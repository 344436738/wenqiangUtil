/**
 * 
 */
package struts.listener;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jdom.JDOMException;

import struts.stauts.strutsBean;
import struts.stauts.strutsXmlUtil;

/**
 * @author msa12
 *2018
 */
public class strutsListener implements ServletContextListener {

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("系统关闭");

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
       
		ServletContext context = arg0.getServletContext();
		String initParameter = context.getInitParameter("struts-config");
		String realPath = context.getRealPath("\\");
		try {
			Map<String, strutsBean> xml = strutsXmlUtil.getXml(realPath+initParameter);
			context.setAttribute("struts", xml);
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("系统初始化加载完毕。。。。。。。。。。。。。");
	}

}
