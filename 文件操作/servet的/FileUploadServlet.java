package com.sinotrans.reportserver.common.servelt;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.sinotrans.framework.core.model.BaseModel;
import com.sinotrans.framework.core.support.CustomBeanWrapper;
import com.sinotrans.framework.core.util.ContextUtils;
import com.sinotrans.reportserver.common.security.UserDetails;
import com.sinotrans.reportserver.manage.service.RsReportTemplateManager;


public class FileUploadServlet extends HttpServlet implements
        javax.servlet.Servlet {
    public FileUploadServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	HashMap parameters = new HashMap();
        byte[] file = null;
        String fileName = null;
    	Object model = null;
    	
    	try {
	    	//依据前台的enctype="multipart/form-data"
	        if (ServletFileUpload.isMultipartContent(request)) {
	        	DiskFileItemFactory dff = new DiskFileItemFactory();
	        	ServletFileUpload sfu = new ServletFileUpload(dff);
	        	//处理请求，将请求中的各种数据(file和非file类)
	            List<FileItem> items = sfu.parseRequest(request);
	            Iterator iter = items.iterator();
	            
	            while (iter.hasNext()) {
                	FileItem item = (FileItem) iter.next();
                	if (item.isFormField()) {
                		//非file类型数据处理
                		String name = item.getFieldName();//控件name属性
                		String value = item.getString();
                		parameters.put(name, new String(value.getBytes("ISO-8859-1"),"UTF-8"));
                	}else{
                		//file类型数据处理
                		fileName = item.getName();//文件包含客户端路径的名称
                		if(fileName!=null && fileName.length()>0){
	                          fileName.replaceAll("/", "\\");
	                          if(fileName.indexOf("\\")>0){
	                              fileName = fileName.substring(fileName.lastIndexOf("\\")+1, fileName.length());
	                          }
	                      }
                		
                		file = IOUtils.toByteArray(item.getInputStream());
                	}
	            }
	            if(parameters.containsKey("model")){
	            	model = Class.forName(parameters.get("model").toString()).newInstance();
	            }
	            
	            CustomBeanWrapper modelWrapper = new CustomBeanWrapper(model);
            	iter = parameters.keySet().iterator();
            	while (iter.hasNext()) {
            		String name = iter.next().toString();
            		if(modelWrapper.isWritableProperty(name)){
            			modelWrapper.setPropertyValue(name, parameters.get(name));
            		}
            	}
            	
            	//类中固定的下载方法“public void uploadBlob(Long id, BaseModel model, byte[] file,String fileName) ”，
            	//使用时固定传递参数：model【Blob字段的Model类名】、service【Blob字段的Manager类名】、*【其他与model变量同名的参数，一同保存更新model】
            	Method cMethod = ContextUtils.getBeanOfType(Class.forName(parameters.get("service").toString())).getClass().getMethod("uploadBlob", new java.lang.Class[]{Long.class,BaseModel.class,byte[].class,String.class});
            	
            	Object m = cMethod.invoke(ContextUtils.getBeanOfType(Class.forName(parameters.get("service").toString())), new Object[]{new Long(0),model,file,fileName}); 
            	response.setHeader("Content-Type", "text/html; charset=utf-8");
                response.getWriter().print("{\"success\":true}");//返回数据JSON类型
	        }
		} catch(Exception e){
			e.printStackTrace();
			 response.getWriter().print("{\"success\":false,\"error\":{'failure'}}");
		}
    }

    public void init() throws ServletException {

    }
}
