/**
 * 
 */
package demo.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import demo.pojo.demoPojo;
import struts.actino.Action;
import struts.stauts.form;

/**
 * @author msa12
 *2018
 */
public class demoAction implements Action{

	/* (non-Javadoc)
	 * @see struts.actino.Action#exec(javax.servlet.http.HttpServletRequest, struts.stauts.form, java.util.Map)
	 */
	public String  exec(HttpServletRequest request, form f,Map<String, String> map) {
	
		demoPojo demo=(demoPojo)f;
		String url="shibai";
		String login=" ß∞‹£°";
		if(demo.getPath().equals("chenggong")){
			 url="chenggong";
			 login="≥…π¶¡À£°";
		}
		request.setAttribute("login",login);
		return map.get(url);
	}

}
