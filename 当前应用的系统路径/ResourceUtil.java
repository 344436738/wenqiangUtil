package com.leimingtech.core.util;

import com.leimingtech.common.utils.DBTypeUtil;
import com.leimingtech.common.utils.LogUtil;
import com.leimingtech.common.utils.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;


/**
 * 项目参数工具类
 * 
 */
public class ResourceUtil {

	private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("sysConfig");

	/**
	 * 获取session定义名称
	 *
	 * @return
	 */
	public static final String getSessionattachmenttitle(String sessionName) {
		return bundle.getString(sessionName);
	}

	/**
	 * 获得请求路径
	 *
	 * @param request
	 * @return
	 */
	public static String getRequestPath(HttpServletRequest request) {
		String requestPath = request.getRequestURI() + "?" + request.getQueryString();
		if(requestPath.indexOf("&") > -1) {// 去掉其他参数
			requestPath = requestPath.substring(0 , requestPath.indexOf("&"));
		}
		requestPath = requestPath.replaceAll("//" , "/");
		requestPath = requestPath.substring(request.getContextPath().length() + 1);// 去掉项目路径
		return requestPath;
	}

	/**
	 * 获取配置文件参数
	 *
	 * @param name
	 * @return
	 */
	public static final String getConfigByName(String name) {
		return bundle.getString(name);
	}

	/**
	 * 获取配置文件参数
	 *
	 * @param name
	 * @return
	 */
	public static final Map<Object , Object> getConfigMap(String path) {
		ResourceBundle bundle = ResourceBundle.getBundle(path);
		Set set = bundle.keySet();
		return com.leimingtech.common.utils.oConvertUtils.SetToMap(set);
	}

	public static String getSysPath() {
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		String temp = path.replaceFirst("file:/" , "").replaceFirst("WEB-INF/classes/" , "");
		String separator = System.getProperty("file.separator");
		String resultPath = temp.replaceAll("/" , separator + separator).replaceAll("%20" , " ");
		return resultPath;
	}

	/**
	 * 获取项目根目录
	 *
	 * @return
	 */
	public static String getPorjectPath() {
		String nowpath; // 当前tomcat的bin目录的路径 如
		// D:\java\software\apache-tomcat-6.0.14\bin
		String tempdir;
		nowpath = System.getProperty("user.dir");
		tempdir = nowpath.replace("bin" , "webapps"); // 把bin 文件夹变到 webapps文件里面
		tempdir += "\\"; // 拼成D:\java\software\apache-tomcat-6.0.14\webapps\sz_pro
		return tempdir;
	}

	public static String getClassPath() {
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		String temp = path.replaceFirst("file:/" , "");
		String separator = System.getProperty("file.separator");
		String resultPath = temp.replaceAll("/" , separator + separator);
		return resultPath;
	}

	public static String getSystempPath() {
		return System.getProperty("java.io.tmpdir");
	}

	public static String getSeparator() {
		return System.getProperty("file.separator");
	}

	public static String getParameter(String field) {
		HttpServletRequest request = com.leimingtech.common.ContextHolderUtils.getRequest();
		return request.getParameter(field);
	}

	/**
	 * 获取数据库类型
	 *
	 * @return
	 * @throws Exception
	 */
	public static final String getJdbcUrl() {
		return DBTypeUtil.getDBType().toLowerCase();
	}

	// update-begin--Author:zhangguoming Date:20140226 for：添加验证码
	/**
	 * 获取随机码的长度
	 *
	 * @return 随机码的长度
	 */
	public static String getRandCodeLength() {
		return bundle.getString("randCodeLength");
	}

	/**
	 * 获取随机码的类型
	 *
	 * @return 随机码的类型
	 */
	public static String getRandCodeType() {
		return bundle.getString("randCodeType");
	}

	// update-end--Author:zhangguoming Date:20140226 for：添加验证码

	/**
	 * 获取图像处理工具类型 return type
	 */
	public static String getImageUtilType() {
		return bundle.getString("imageUtilType");
	}

	@Test
	public void test() {
		com.leimingtech.common.utils.LogUtil.info(getPorjectPath());
		LogUtil.info(getSysPath());
	}

	/**
	 * 获取图像处理类型 return type
	 */
	public static String getImageCreatortype() {
		return bundle.getString("imageCreatortype");
	}

	/**
	 * 获取LMCMS站点静态文件发布路径 liuzhen 2014年6月23日 10:52:13
	 *
	 * @return
	 */
	public static String getCMSStaticFilesPath() {
		String staticFilesPath = bundle.getString("lmcms.site.staticfiles.path").trim();
		if(com.leimingtech.common.utils.StringUtils.isNotEmpty(staticFilesPath)) {
			File file = new File(staticFilesPath);
			if(!file.exists()) {
				file.mkdirs();
			}
			if(!file.exists()) {
				return null;
			}
			return staticFilesPath;
		}
		return null;
	}

	/**
	 * 获取LMCMS站点文件上传路径 liuzhen 2014年6月23日 10:52:13
	 *
	 * @return
	 */
	public static String getCMSUploadFilesPath() {
		String uploadFilesPath = bundle.getString("lmcms.site.uploadfiles.path").trim();
		if(StringUtils.isNotEmpty(uploadFilesPath)) {
			File file = new File(uploadFilesPath);
			if(!file.exists()) {
				file.mkdirs();
			}
			if(!file.exists()) {
				return null;
			}
			return uploadFilesPath;
		}
		return null;
	}

	/**
	 * 获取LMCMS站点文件上传路径是否分离 liuzhen 2014年6月23日 10:52:13
	 *
	 * @return
	 */
	public static boolean getCMSUploadFilesSeparation() {
		String separation = bundle.getString("lmcms.site.uploadfiles.separation.flag").trim();
		return BooleanUtils.toBoolean(separation);
	}

	/**
	 * 获取cms前台路径
	 * @return
     */
	public static String getCMSFrontPath(){
		String frontPath = bundle.getString("front.server");
		return frontPath;
	}

	/**
	 * 获取cms后台路径
	 * @return
	 */
	public static String getCMSAdminPath(){
		String adminPath = bundle.getString("admin.server");
		return adminPath;
	}

}
