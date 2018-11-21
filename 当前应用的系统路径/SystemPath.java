package com.leimingtech.core.util;

import java.io.File;


/**
 * 
 * @author wanye
 * @date Dec 14, 2008
 * @version v 1.0
 * @description 得到当前应用的系统路径
 */
public class SystemPath {

	public static String getSysPath()
	{
//		String path= SystemPath.class.getResource("/").getFile().toString();
		String path= getRootPath();
//		String path= Thread.currentThread().getContextClassLoader().getResource("").toString();
//		String temp=path.replaceFirst("file:/", "").replaceFirst("WEB-INF/classes/", "");
//		String separator= System.getProperty("file.separator");
//		String resultPath=temp.replaceAll("/", separator+separator);
		return path;
	}
	
	public static String getRootPath() {
		String classPath = SystemPath.class.getClassLoader().getResource("/").getPath();
		String rootPath = "";
		// windows下
		if ("\\".equals(File.separator)) {
			rootPath = classPath.substring(1, classPath.indexOf("WEB-INF/classes"));
			rootPath = rootPath.replace("/", "\\");
		}
		// linux下
		if ("/".equals(File.separator)) {
			rootPath = classPath.substring(0, classPath.indexOf("WEB-INF/classes"));
			rootPath = rootPath.replace("\\", "/");
		}
		return rootPath;
	}
	
	public static String getClassPath()
	{
		String path= Thread.currentThread().getContextClassLoader().getResource("").toString();
		String temp=path.replaceFirst("file:/", "");
		String separator= System.getProperty("file.separator");
		String resultPath=temp.replaceAll("/", separator+separator);
		return resultPath;
	}
	public static String getSystempPath()
	{
		return System.getProperty("java.io.tmpdir");
	}
	public static String getSeparator()
	{
		return System.getProperty("file.separator");
	}
	
	public static void main(String[] args){
		LogUtil.info(getSysPath());
		LogUtil.info(System.getProperty("java.io.tmpdir"));
		LogUtil.info(getSeparator());
		LogUtil.info(getClassPath());
	}
}
