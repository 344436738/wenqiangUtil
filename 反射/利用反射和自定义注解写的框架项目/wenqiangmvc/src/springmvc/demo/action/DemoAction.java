package springmvc.demo.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import springmvc.annotation.WQAutowired;
import springmvc.annotation.WQController;
import springmvc.annotation.WQRequestMapping;
import springmvc.annotation.WQRequestParam;
import springmvc.demo.service.IDemoService;




@WQController
@WQRequestMapping("/web")
public class DemoAction {
	@WQAutowired(value="demoservice") 
	private IDemoService idemoservice;
	@WQRequestMapping("/query")
	public void query(HttpServletRequest request,HttpServletResponse response,
			@WQRequestParam("name") String name,@WQRequestParam("test") String test) {
		String result = idemoservice.query(name);
		try {
			response.getWriter().write("name:"+result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
