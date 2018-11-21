package springmvc.serlvet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Url;
import org.jdom.Element;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry.Entry;

import springmvc.annotation.WQAutowired;
import springmvc.annotation.WQController;
import springmvc.annotation.WQRequestMapping;
import springmvc.annotation.WQRequestParam;
import springmvc.annotation.WQService;

/**
 * Servlet implementation class mvcSerlvet
 */
@WebServlet("/mvcSerlvet")
public class mvcSerlvet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Properties profile=new Properties();
	
	private List<String> clz=new  ArrayList<String>();
	
	private  Map<String,Object> ioc=new HashMap<String, Object>();
	
	
	private List<Handler> handlerMapping=new ArrayList<Handler>();
	
       
    /**
     *
     */
    public mvcSerlvet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		System.out.println("自定义的Springmvc加载完毕！");
		getProfile(config.getInitParameter("contextConfigLocation"));
		
		saveClass(profile.getProperty("springmvc.url"));
		
		saveClassMap();
		
		doActowired();
		
		new ConcurrentHashMap();
		
		initHandlerMapping();
		
	}

	
	/**
	 * 
	 */
	private void initHandlerMapping() {
		if(ioc.isEmpty()){
			return;
		}
		for(java.util.Map.Entry<String, Object> entry:ioc.entrySet()){
			Class<? extends Object> class1 = entry.getValue().getClass();
			if(!class1.isAnnotationPresent(WQController.class)){
				continue;
			}
			
			String url="";
			if(class1.isAnnotationPresent(WQRequestMapping.class)){
				url=class1.getAnnotation(WQRequestMapping.class).value();
			}
			Method[] methods = class1.getMethods();
			for(Method meth:methods){
				if(!meth.isAnnotationPresent(WQRequestMapping.class)){
					continue;
				}
				WQRequestMapping annotation = meth.getAnnotation(WQRequestMapping.class);
				String regex = ("/"+url+annotation.value()).replaceAll("/+", "/");
				Pattern pattern = Pattern.compile(regex);
				handlerMapping.add(new Handler(pattern,entry.getValue(),meth));
			 }
				
			
		}
		
	}

	/**
	 * 扫描Actowired注解   并注入 
	 */
	private void doActowired() {
	   if(ioc.isEmpty()){
		   return;
	   }
	   for(java.util.Map.Entry<String, Object> enty:ioc.entrySet()){
		   Field[] fields = enty.getValue().getClass().getDeclaredFields();
		   for(Field field:fields){
			    if(!field.isAnnotationPresent(WQAutowired.class)){
			    	continue;
			    }
			    WQAutowired annotation = field.getAnnotation(WQAutowired.class);
			    String str = annotation.value().trim();
			    field.setAccessible(true);
			    try {
					field.set(enty.getValue(),ioc.get(str));
					field.setAccessible(false);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    
			    
		    }
	  }
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	    this.doPost(req, resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			doDispatch(req, resp);
		}catch (Exception e) {
			resp.getWriter().write("500 Exception,Detail:\r\n"+Arrays.toString(e.getStackTrace()));
		}
	
	}
	
	/**
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
	      
	      Handler handler =getHandler(req);
	      if(handler == null) {
				resp.getWriter().write("404 not found");
			}
		
	      Class<?>[] parameterTypes = handler.method.getParameterTypes();
	      Object[] object=new Object[parameterTypes.length];
	      Map<String, String[]> parameterMap = req.getParameterMap();
	      for(java.util.Map.Entry<String, String[]> entry:parameterMap.entrySet()){
	    	  String value = Arrays.toString(entry.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
	    	  if(!handler.paramIndexMapping.containsKey(entry.getKey())){
	    		  continue;
	    	  }
	    	  int index = handler.paramIndexMapping.get(entry.getKey());
	    	  object[index]=convert(parameterTypes[index],value);
	      }
	      int reqIndex = handler.paramIndexMapping.get(HttpServletRequest.class.getName());
	        object[reqIndex] = req;
			int respIndex = handler.paramIndexMapping.get(HttpServletResponse.class.getName());
			object[respIndex] = resp;
			
			handler.method.invoke(handler.controller, object);
	}

	/**
	 * @param class1
	 * @param value
	 * @return
	 */
	private Object convert(Class<?> class1, String value) {
		if(Integer.class == class1) {
			return Integer.valueOf(value);
		}
		return value;
	}

	/**
	 * @param path
	 * @return
	 */
	private Handler getHandler(HttpServletRequest req) {
		if(handlerMapping.isEmpty()){
			return null;
		}
		String url = req.getRequestURI();
		String contextPath = req.getContextPath();
		url = url.replace(contextPath, "").replaceAll("/+", "/");
		for(Handler handler:handlerMapping){
			Matcher matcher = handler.pattern.matcher(url);
			if(!matcher.matches()) {
				continue;
			}
			return handler;
		}
		// TODO Auto-generated method stub
		return null;
	}

	class Handler{
		
		protected Object controller;
		
		protected Method method;
		
		protected Pattern pattern;
		
		protected Map<String,Integer> paramIndexMapping;
		

		/**
		 * @param pattern
		 * @param value
		 * @param meth
		 */
		public Handler(Pattern pattern, Object value, Method meth) {
			// TODO Auto-generated constructor stub
			this.controller=value;
			this.method=meth;
			this.pattern=pattern;
			paramIndexMapping=new HashMap<String, Integer>();
			putParamIndexMapping(meth);
			
		}
		  
		/**
		 * @param meth
		 */
		public void putParamIndexMapping(Method meth) {
			Annotation[][] parameterAnnotations = meth.getParameterAnnotations();
			for(int i=0;i<parameterAnnotations.length;i++){
				for(Annotation a:parameterAnnotations[i]){
					if(a instanceof WQRequestParam){
						String param=((WQRequestParam)a).value();
						if(!"".equals(param.trim())){
							paramIndexMapping.put(param,i);
						}
						
					}
				}
			}
			Class<?>[] parameterTypes = meth.getParameterTypes();
			for(int j=0;j<parameterTypes.length;j++){
				if(parameterTypes[j]==HttpServletRequest.class||parameterTypes[j]==HttpServletResponse.class){
					paramIndexMapping.put(parameterTypes[j].getName(), j);
				}
			}
			
		}

		
	}

	public void getProfile(String path){
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("../"+path);
		try {
			profile.load(resourceAsStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				resourceAsStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	

	/*
	 * 扫描包路径下的 文件保存集合当中
	 * **/
	public void saveClass(String packPath){
        String  path="/"+packPath.replace(".", "/");
        URL url=this.getClass().getClassLoader().getResource(path);
		File dir=new File(url.getFile());
		for(File file:dir.listFiles()){
			if(file.isDirectory()){
				saveClass(packPath+"."+file.getName());
			}else{
		     	String clazz=packPath+"."+file.getName().replace(".class","");
		     	clz.add(clazz);
			}  
			
			
		}
		
		
	}
	
	
	/***
	 * 扫描class到map当中
	 * 
	 * */
	public void  saveClassMap(){
		if(clz.isEmpty()){
			return;
		}
		for(String clas:clz){
			Class class1;
			try {
				class1 = Class.forName(clas);
				if(class1.isAnnotationPresent(WQController.class)){
					ioc.put(lowerFirst(class1.getSimpleName()),class1.newInstance());
				}
				else if(class1.isAnnotationPresent(WQService.class)){
					WQService service = (WQService) class1.getAnnotation(WQService.class);
					String servleName=service.value();
					if(!"".equals(servleName.trim())){
						ioc.put(servleName, class1.newInstance());
					}else{
						ioc.put(lowerFirst(class1.getSimpleName()),class1.newInstance());
					}
					Class[] interfaces = class1.getInterfaces();
					for(Class inter:interfaces){
						ioc.put(inter.getName(),class1.newInstance());
					}
				}else{
					continue;
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private String lowerFirst(String str) {
		char[] chars = str.toCharArray();
		chars[0] +=32;
		return String.valueOf(chars);
	}
}
