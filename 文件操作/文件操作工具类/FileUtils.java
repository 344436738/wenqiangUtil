package com.leimingtech.core.util;

import com.leimingtech.common.ContextHolderUtils;
import org.apache.log4j.Logger;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 文件操作工具类
 * @author 
 *
 */
public class FileUtils {
	private static final Logger logger = Logger.getLogger(FileUtils.class);
	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtend(String filename) {
		return getExtend(filename, "");
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtend(String filename, String defExt) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');

			if ((i > 0) && (i < (filename.length() - 1))) {
				return (filename.substring(i+1)).toLowerCase();
			}
		}
		return defExt.toLowerCase();
	}

	/**
	 * 获取文件名称[不含后缀名]
	 * 
	 * @param
	 * @return String
	 */
	public static String getFilePrefix(String fileName) {
		int splitIndex = fileName.lastIndexOf(".");
		return fileName.substring(0, splitIndex).replaceAll("\\s*", "");
	}
	
	/**
	 * 获取文件名称[不含后缀名]
	 * 不去掉文件目录的空格
	 * @param
	 * @return String
	 */
	public static String getFilePrefix2(String fileName) {
		int splitIndex = fileName.lastIndexOf(".");
		return fileName.substring(0, splitIndex);
	}
	
	
	
	/**
	 * 获得文件类型
	 * @param file
	 * @return
	 */
    public static String getMimeType(File file) {
        String mimetype = "";
        if (file.exists()) {
//            URLConnection uc = new URL("file://" + file.getAbsolutePath()).openConnection();
//            String mimetype = uc.getContentType();
//            MimetypesFIleTypeMap gives PNG as application/octet-stream, but it seems so does URLConnection
//            have to make dirty workaround
            if (getSuffix(file.getName()).equalsIgnoreCase("png")) {
                mimetype = "image/png";
            } else {
                javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
                mimetype  = mtMap.getContentType(file);
            }
        }
        System.out.println("mimetype: " + mimetype);
        return mimetype;
    }

	/**
	 * 获得文件后缀名
	 * @param filename
	 * @return
	 */
    public static String getSuffix(String filename) {
        String suffix = "";
        int pos = filename.lastIndexOf('.');
        
        if (pos > 0 && pos < filename.length() - 1) {
        	
        		
        		suffix = filename.substring(pos + 1);
        		if (suffix.equals("jpg") || suffix.equals("jpeg")) {
        			suffix= "jpg";
        		} else if (suffix.equals("png")) {
        			suffix= "png";
        		} else if (suffix.equals("gif")) {
        			suffix= "gif";
        		} else if (suffix.equals("html") || suffix.equals("shtml") || suffix.equals("htm")) {
        			suffix= "html";
        		} else if (suffix.equals("swf")) {
        			suffix= "swf";
        		} else if (suffix.equals("txt")) {
        			suffix= "txt";
        		} else if (suffix.equals("sql")) {
        			suffix= "sql";
        		}else {
        			suffix= "unknow";
        		}

        	
        }else{
        	suffix="folder";
        }
        //System.out.println("suffix: " + suffix);
        return suffix;
    }
	
	/**
	 * 文件复制
	 *方法摘要：这里一句话描述方法的用途
	 *@param
	 *@return void
	 */
	public static void copyFile(String inputFile,String outputFile) throws FileNotFoundException{
		File sFile = new File(inputFile);
		File tFile = new File(outputFile);
		FileInputStream fis = new FileInputStream(sFile);
		FileOutputStream fos = new FileOutputStream(tFile);
		int temp = 0;  
		byte[] buf = new byte[10240];
        try {  
        	while((temp = fis.read(buf))!=-1){   
        		fos.write(buf, 0, temp);   
            }   
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally{
            try {
            	fis.close();
            	fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        } 
	}
	/**
	 * 判断文件是否为图片<br>
	 * <br>
	 * 
	 * @param filename
	 *            文件名<br>
	 *            判断具体文件类型<br>
	 * @return 检查后的结果<br>
	 * @throws Exception
	 */
	public static boolean isPicture(String filename) {
		// 文件名称为空的场合
		if (oConvertUtils.isEmpty(filename)) {
			// 返回不和合法
			return false;
		}
		// 获得文件后缀名
		//String tmpName = getExtend(filename);
		String tmpName = filename;
		// 声明图片后缀名数组
		String imgeArray[][] = { { "bmp", "0" }, { "dib", "1" },
				{ "gif", "2" }, { "jfif", "3" }, { "jpe", "4" },
				{ "jpeg", "5" }, { "jpg", "6" }, { "png", "7" },
				{ "tif", "8" }, { "tiff", "9" }, { "ico", "10" } };
		// 遍历名称数组
		for (int i = 0; i < imgeArray.length; i++) {
			// 判断单个类型文件的场合
			if (imgeArray[i][0].equals(getSuffix(tmpName.toLowerCase()))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断文件是否为DWG<br>
	 * <br>
	 * 
	 * @param filename
	 *            文件名<br>
	 *            判断具体文件类型<br>
	 * @return 检查后的结果<br>
	 * @throws Exception
	 */
	public static boolean isDwg(String filename) {
		// 文件名称为空的场合
		if (oConvertUtils.isEmpty(filename)) {
			// 返回不和合法
			return false;
		}
		// 获得文件后缀名
		String tmpName = getExtend(filename);
		// 声明图片后缀名数组
		if (tmpName.equals("dwg")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 删除指定的文件
	 * 
	 * @param strFileName
	 *            指定绝对路径的文件名
	 * @return 如果删除成功true否则false
	 */
	public static boolean delete(String strFileName) {
		File fileDelete = new File(strFileName);

		if (!fileDelete.exists() || !fileDelete.isFile()) {
			LogUtil.info("错误: " + strFileName + "不存在!");
			return false;
		}

		LogUtil.info("--------成功删除文件---------"+strFileName);
		return fileDelete.delete();
	}
	/**
	 * 创建文件
	 * @param in
	 * @param filePath
	 */
	public static void createFile(InputStream in, String filePath) {
		if(in==null) throw new RuntimeException("create file error: inputstream is null");
		int potPos = filePath.lastIndexOf('/') + 1;
		String folderPath = filePath.substring(0, potPos);
		createFolder(folderPath);
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(filePath);
			byte[] by = new byte[1024];
			int c;
			while ((c = in.read(by)) != -1) {
				outputStream.write(by, 0, c);
			}
		} catch (IOException e) {
			e.getStackTrace().toString();
		}
		try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建文件夹
	 * 
	 * @param filePath
	 */
	public static void createFolder(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception ex) {
			System.err.println("Make Folder Error:" + ex.getMessage());
		}
	}
	/** 
	 * 删除目录（文件夹）如果文件夹下没有文件时
	 * @param   sPath 被删除目录的文件路径 
	 * @return  目录删除成功返回true，否则返回false 
	 */  
	public static boolean deleteDirectory(String sPath) {  
		boolean flag = false;  
	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
	    if (!sPath.endsWith(File.separator)) {  
	        sPath = sPath + File.separator;  
	    }  
	    File dirFile = new File(sPath);  
	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
	    if (!dirFile.exists() || !dirFile.isDirectory()) {  
	        return flag;  
	    }  
	    //删除文件夹  
	    File[] files = dirFile.listFiles();  
	    if(files.length==0){
	    	dirFile.delete();
	    	flag = true;
	    }
	    return flag;  
	}
	
	/**
	 * 下载文件
	 * @param path 文件路径
	 * @param response
	 * @return
	 */
	public static HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            String userAgent=ContextHolderUtils.getRequest().getHeader("USER-AGENT");
            String new_filename = URLEncoder.encode(filename, "UTF8");  
			// 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
			String rtn = "filename=\"" + new_filename + "\"";
			if (userAgent != null) {
				userAgent = userAgent.toLowerCase();
				// IE浏览器，只能采用URLEncoder编码
				if (userAgent.indexOf("msie") != -1) {
					rtn = "filename=\"" + new_filename + "\"";
				}
				// Opera浏览器只能采用filename*
				else if (userAgent.indexOf("opera") != -1) {
					rtn = "filename*=UTF-8''" + new_filename;
				}
				// Safari浏览器，只能采用ISO编码的中文输出
				else if (userAgent.indexOf("safari") != -1) {
					rtn = "filename=\"" + new String(filename.getBytes("UTF-8"), "ISO8859-1") + "\"";
				}
				// Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
				else if (userAgent.indexOf("applewebkit") != -1) {
					new_filename = MimeUtility.encodeText(filename, "UTF8", "B");
					rtn = "filename=\"" + new_filename + "\"";
				}
				// FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
				else if (userAgent.indexOf("mozilla") != -1) {
					rtn = "filename*=UTF-8''" + new_filename;
				}
			}
            
            
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;" + rtn);
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }
	
	/**
	 * 获取路径中文件名称，不含后缀名
	 * @param path
	 * @return
	 */
	public static String getPathName(String path){
		int lastxie = path.lastIndexOf("/");
		String str = path.substring(lastxie, path.length());
		int lastpoint = str.lastIndexOf(".");
		String filename = str.substring(str.indexOf("/")+1,lastpoint);
		return filename;
	}
	
	
	/***
	 * 获取一个临时文件
	 * @return
	 */
	public static File getTmpFile() {
		File tmpDir = org.apache.commons.io.FileUtils.getTempDirectory();
		String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
		return new File(tmpDir, tmpFileName);
	}
}
