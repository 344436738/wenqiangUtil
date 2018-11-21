package struts.serlvet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import struts.actino.Action;
import struts.stauts.form;
import struts.stauts.strutsBean;
import struts.stauts.strutsXmlUtil;

/**
 * Servlet implementation class ActionSerlvet
 */
@WebServlet("/ActionSerlvet")
public class ActionSerlvet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActionSerlvet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String path =request.getServletPath().split("\\.")[0];
		 Map<String,strutsBean> map =(Map<String,strutsBean>)this.getServletContext().getAttribute("struts");
		 strutsBean strutsBean = map.get(path);
		 
		 Action action=null;
		 String url="";
		 try{
			 form class1 = strutsXmlUtil.getClass(request, strutsBean.getFormName());
			 Class forName = Class.forName( strutsBean.getActionType());
			 action=(Action)forName.newInstance();
			 url=action.exec(request, class1, strutsBean.getMap1());
			 RequestDispatcher dispatcher = request.getRequestDispatcher(url);
			 dispatcher.forward(request, response);
		 }catch(Exception e){
			 System.out.println("³öÏÖ´íÎó£¡");
		 }
		
		
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

}
