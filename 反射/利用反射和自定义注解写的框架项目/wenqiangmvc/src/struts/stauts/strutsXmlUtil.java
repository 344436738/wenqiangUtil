/**
 * 
 */
package struts.stauts;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.sun.media.sound.EmergencySoundbank;

/**
 * @author 文强
 *2018
 */
public class strutsXmlUtil {
	
	
   public static Map<String,strutsBean>  getXml(String path) throws JDOMException, IOException{
	   
	   SAXBuilder  builder=new SAXBuilder();
	   Map<String,strutsBean> map1=new HashMap<String, strutsBean>();
	   Document document = builder.build(new File(path));
	   Element rootElement = document.getRootElement();
	   Element element = rootElement.getChild("action-mapping");
	   List<Element> childrenList = element.getChildren();
	   for(Element list:childrenList){
		   strutsBean ben=new strutsBean();
		 
		   String actionName=list.getAttributeValue("name");
		   ben.setActionName(actionName); 
		   ben.setActionType(list.getAttributeValue("type"));
		   String  actionPath=list.getAttributeValue("path");
		   ben.setActionPath(actionPath);
		   Element formBenas = rootElement.getChild("formbeans");
		   List<Element> formElent = formBenas.getChildren();
		   for(Element form:formElent){
			   if(actionName.equals(form.getAttributeValue("name"))){
				   ben.setFormName(form.getAttributeValue("class"));
				   break;
				   
			   }
			   
			    
		   }
		   Map<String,String> map2=new HashMap<String, String>();
		   List<Element> forword = list.getChildren();
		   for(Element word:forword){
			   map2.put(word.getAttributeValue("name"),word.getAttributeValue("viwe"));
		   }
		   ben.setMap1(map2);
		   map1.put(actionPath, ben);
		   
	   }
	   
	    
	   
	   return map1;
   }
	 
   public static form getClass(HttpServletRequest request,String claz) throws ClassNotFoundException{
	   
	   Class forName = Class.forName(claz);
	   form m=null;
	   try{
		  m = (form)forName.newInstance();
		  Field[] declaredFields = forName.getDeclaredFields();
		  for(Field field:declaredFields){
			   field.setAccessible(true);
			   field.set(m,request.getParameter(field.getName()));
			   field.setAccessible(false);
		  }
	   }catch(Exception e){
		   System.out.println("实体反射错误！");
	   }
	   
	   return m;
   }
   
}
