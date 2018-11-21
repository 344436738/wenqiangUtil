package com.sinotrans.reportserver.common.servelt;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sinotrans.framework.core.util.ContextUtils;
import com.sun.net.httpserver.HttpContext;

public class FileDownloadServlet extends HttpServlet implements
		javax.servlet.Servlet {
	public FileDownloadServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
		
		String id = request.getParameter("id");
		String service = request.getParameter("service");
		Object manager = null;
		try {
			//类中固定的下载方法“public Object[] downloadBlob(Long id) ”，
        	//使用时固定传递参数：model【Blob字段的Model类名】、service【Blob字段的Manager类名】、id【要下载文件的model主键】
			Method getFileMethod = ContextUtils.getBeanOfType(Class.forName(service)).getClass().getMethod("downloadBlob",new java.lang.Class[] { Long.class });
			Object[] blob = null;
			Object t = getFileMethod.invoke(ContextUtils.getBeanOfType(Class.forName(service)),new Object[] { new Long(id) });
			if(t!=null){
				blob = (Object[])t;
			}
				
			if(blob!=null && blob.length>0){
				String fileName = (String)blob[0];
				byte[] file = (byte[])blob[1];
				response.addHeader("Content-Disposition", "attachment;filename=" +new String( fileName.getBytes("gb2312"), "ISO8859-1" ));
				response.addHeader("Content-Length", "" + file.length);
				response.setContentType("application/octet-stream");

				ServletOutputStream os = response.getOutputStream();

				os.write(file);
				os.flush();
				os.close();
				response.setStatus(HttpServletResponse.SC_OK);
				response.flushBuffer();
			}
			

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	public void init() throws ServletException {

	}
}
