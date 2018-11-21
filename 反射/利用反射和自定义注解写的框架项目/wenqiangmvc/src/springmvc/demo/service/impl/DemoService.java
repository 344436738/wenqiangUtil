package springmvc.demo.service.impl;

import springmvc.annotation.WQService;
import springmvc.demo.service.IDemoService;

@WQService(value="demoservice")
public class DemoService implements IDemoService{

	@Override
	public String query(String name) {
		// TODO Auto-generated method stub
		return name;
	}


	
}
