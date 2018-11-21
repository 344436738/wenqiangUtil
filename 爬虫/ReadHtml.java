package com.leimingtech.core.util;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;



/**
 * @author luohaiming
 * 
 * @version 解析html
 */
public class ReadHtml {
	private static String ENCODE = "GB2312";

	public static String getHtml(String htmlFile) {
		String html=null;
		try {
			 String szContent = openFile(htmlFile);
			 Parser parser = Parser.createParser(szContent, ENCODE);
			 NodeFilter filter = new TagNameFilter ("HTML");  
	         NodeList nodes = parser.extractAllNodesThatMatch(filter); 		 
	         if(nodes!=null) {  
	             Node textnode = (Node) nodes.elementAt(0);  
	             html=textnode.toHtml();  
	           }      
		} catch (Exception e) {
			e.printStackTrace();
		}
        return html;
	}

	private static String openFile(String szFileName) {
		try {
			BufferedReader bis = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(szFileName)), ENCODE));
			String szContent = "";
			String szTemp="";

			while ((szTemp = bis.readLine()) != null) {
				szContent += szTemp + "<br/>";
			}
			bis.close();
			return szContent;
		} catch (Exception e) {
			return "";
		}
	}
}