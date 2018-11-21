package com.leimingtech.core.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class FileUtil {
	public static boolean writeText(String fileName, String content) {
		return writeText(fileName, content, "GBK");
	}

	public static void main(String[] args) {
		// Product.editBook("E:/eclipse/workspace/Shop/UI/html/design/sysDesign/159/2011/12/30/901/111662/flash/save.xml",
		// "300", "899", "100", "100", "1", "1", "1", "1", "1", "1", "1",
		// "1","1.2","A");
		// System.out.println(Application.getCurrentSiteName());
		// refreshFileList("D:/datas");
	}

	public static boolean writeText(String fileName, String content,
			String encoding) {
		try {
			FileOutputStream fis;
			fis = new FileOutputStream(fileName);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fis,
					encoding));
			bw.write(content);
			bw.flush();
			bw.close();
			fis.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static byte[] readByte(String fileName) {
		try {
			FileInputStream fis = new FileInputStream(fileName);
			byte[] r = new byte[fis.available()];
			fis.read(r);
			fis.close();
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] readByte(File f) {
		try {
			FileInputStream fis = new FileInputStream(f);
			byte[] r = new byte[fis.available()];
			fis.read(r);
			fis.close();
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean writeByte(String fileName, byte[] b) {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(b);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean writeByte(File f, byte[] b) {
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(b);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String readText(File f) {
		return readText(f, "GBK");
	}

	public static String readText(File f, String encoding) {
		try {
			InputStream is = new FileInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					encoding));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			br.close();
			is.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String readText(String fileName) {
		return readText(fileName, "GBK");
	}

	public static String readText(String fileName, String encoding) {
		try {
			InputStream is = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					encoding));
			StringBuffer sb = new StringBuffer();
			String line;
			int c = br.read();
			if (!encoding.equalsIgnoreCase("utf-8") || c != 65279) {
				sb.append((char) c);
			}
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			br.close();
			is.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 不管路径是文件还是文件夹，都删掉
	 * 
	 * @param path
	 * @return
	 */
	public static boolean delete(String path) {
		File file = new File(path);
		return delete(file);
	}

	/**
	 * 不管路径是文件还是文件夹，都删掉
	 * 
	 * @param file
	 * @return
	 */
	public static boolean delete(File file) {
		if (!file.exists()) {
			System.out.println("文件或文件夹不存在：" + file);
			return false;
		}
		if (file.isFile()) {
			return file.delete();
		} else {
			return FileUtil.deleteDir(file);
		}
	}

	/**
	 * 删除文件夹，且删除自己本身
	 * 
	 * @param path
	 * @return boolean
	 */
	public static boolean deleteDir(File dir) {
		try {
			return deleteFromDir(dir) && dir.delete(); // 先删除完里面所有内容再删除空文件夹
		} catch (Exception e) {
			System.out.println("删除文件夹操作出错");
			// e.printStackTrace();
			return false;
		}
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static boolean mkdir(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return true;
	}

	/**
	 * 文件名支持使用正则表达式（文件路径不支持正则表达式）
	 */
	public static boolean deleteEx(String fileName) {
		int index1 = fileName.lastIndexOf("\\");
		int index2 = fileName.lastIndexOf("/");
		index1 = index1 > index2 ? index1 : index2;
		String path = fileName.substring(0, index1);
		String name = fileName.substring(index1 + 1);
		File f = new File(path);
		if (f.exists() && f.isDirectory()) {
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (Pattern.matches(name, f.getName())) {
					files[i].delete();
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 删除文件夹里面的所有文件,但不删除自己本身
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteFromDir(String dirPath) {
		File file = new File(dirPath);
		return deleteFromDir(file);
	}

	/**
	 * 删除文件夹里面的所有文件,但不删除自己本身
	 * 
	 * @param file
	 * @return
	 */
	public static boolean deleteFromDir(File dir) {
		if (!dir.exists()) {
			System.out.println("文件夹不存在：" + dir);
			return false;
		}
		if (!dir.isDirectory()) {
			System.out.println(dir + "不是文件夹");
			return false;
		}
		File[] tempList = dir.listFiles();
		for (int i = 0; i < tempList.length; i++) {
			if (!delete(tempList[i])) {
				return false;
			}
		}
		return true;
	}

	private static boolean copy(String oldPath, String newPath,
			FileFilter filter) {
		File oldFile = new File(oldPath);
		File[] oldFiles = oldFile.listFiles(filter);
		boolean flag = true;
		if (oldFiles != null) {
			for (int i = 0; i < oldFiles.length; i++) {
				if (!copy(oldFiles[i], newPath + "/" + oldFiles[i].getName())) {
					flag = false;
				}
			}
		}
		return flag;
	}

	public static boolean copy(String oldPath, String newPath) {
		File oldFile = new File(oldPath);
		return copy(oldFile, newPath);
	}

	public static boolean copy(File oldFile, String newPath) {
		if (!oldFile.exists()) {
			System.out.println("文件或者文件夹不存在：" + oldFile);
			return false;
		}
		if (oldFile.isFile()) {
			return copyFile(oldFile, newPath);
		} else {
			return copyDir(oldFile, newPath);
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldFile
	 * @param newPath
	 * @return boolean
	 */
	public static boolean copyFile(File oldFile, String newPath) {
		File nfile = new File(newPath);
		if (!nfile.getParentFile().exists()) {
			nfile.getParentFile().mkdirs();
		}
		if (!oldFile.exists()) { // 文件存在时
			System.out.println("文件不存在：" + oldFile);
			return false;
		}
		if (!oldFile.isFile()) { // 文件存在时
			System.out.println(oldFile + "不是文件");
			return false;
		}
		try {
			int byteread = 0;
			InputStream inStream = new FileInputStream(oldFile); // 读入原文件
			FileOutputStream fs = new FileOutputStream(newPath);
			byte[] buffer = new byte[1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}
			fs.close();
			inStream.close();
		} catch (Exception e) {
			System.out.println("复制单个文件" + oldFile.getPath() + "操作出错");
			// e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 复制单个文件
	 */
	public static boolean copyFile(File oldFile, File nfile) {
		if (!nfile.getParentFile().exists()) {
			nfile.getParentFile().mkdirs();
		}
		if (!oldFile.exists()) { // 文件存在时
			System.out.println("文件不存在：" + oldFile);
			return false;
		}
		if (!oldFile.isFile()) { // 文件存在时
			System.out.println(oldFile + "不是文件");
			return false;
		}
		try {
			int byteread = 0;
			InputStream inStream = new FileInputStream(oldFile); // 读入原文件
			FileOutputStream fs = new FileOutputStream(nfile);
			byte[] buffer = new byte[1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}
			fs.close();
			inStream.close();
		} catch (Exception e) {
			System.out.println("复制单个文件" + oldFile.getPath() + "操作出错");
			// e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldDir
	 * @param newPath
	 * @return boolean
	 */
	public static boolean copyDir(File oldDir, String newPath) {
		if (!oldDir.exists()) { // 文件存在时
			System.out.println("文件夹不存在：" + oldDir);
			return false;
		}
		if (!oldDir.isDirectory()) { // 文件存在时
			System.out.println(oldDir + "不是文件夹");
			return false;
		}
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File[] files = oldDir.listFiles();
			File temp = null;
			for (int i = 0; i < files.length; i++) {
				temp = files[i];
				if (temp.isFile()) {
					if ("Thumbs.db".equals(temp.getName()))
						continue;
					if (!FileUtil
							.copyFile(temp, newPath + "/" + temp.getName())) {
						return false;
					}
				} else if (temp.isDirectory()) {// 如果是子文件夹
					if (!FileUtil.copyDir(temp, newPath + "/" + temp.getName())) {
						return false;
					}
				}
			}
			return true;
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			// e.printStackTrace();
			return false;
		}
	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public static boolean move(String oldPath, String newPath) {
		return copy(oldPath, newPath) && delete(oldPath);
	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldFile
	 * @param newPath
	 */
	public static boolean move(File oldFile, String newPath) {
		return copy(oldFile, newPath) && delete(oldFile);
	}

	public static void serialize(Serializable obj, String fileName) {
		try {
			FileOutputStream f = new FileOutputStream(fileName);
			ObjectOutputStream s = new ObjectOutputStream(f);
			s.writeObject(obj);
			s.flush();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] serialize(Serializable obj) {
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ObjectOutputStream s = new ObjectOutputStream(b);
			s.writeObject(obj);
			s.flush();
			s.close();
			return b.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object unserialize(String fileName) {
		try {
			FileInputStream in = new FileInputStream(fileName);
			ObjectInputStream s = new ObjectInputStream(in);
			Object o = s.readObject();
			s.close();
			return o;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object unserialize(byte[] bs) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(bs);
			ObjectInputStream s = new ObjectInputStream(in);
			Object o = s.readObject();
			s.close();
			return o;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 保存远程图片到本地
	 * 
	 * @param photoUrl
	 * @param fileName
	 * @return
	 */
	public static boolean saveUrlAs(String photoUrl, String fileName) {
		try {
			URL url = new URL(photoUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			DataInputStream in = new DataInputStream(
					connection.getInputStream());
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					fileName));
			byte[] buffer = new byte[4096];
			int count = 0;
			while ((count = in.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}
			out.close();
			in.close();
			return true;

		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	/**
	 * 取得文件大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSizes(File f) throws Exception {
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else {
			f.createNewFile();
			System.out.println("文件不存在");
		}
		return s;
	}
	
	/**
	 * 取得文件夹大小
	 * 递归
	 * @param f
	 * @return size
	 */
	public static long getFileSize(File f){
		long size = 0;
		File[] flist = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return result list 11.00 B or 11.00 K or 11.00 M or 11.00 G
	 */
	public static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 递归求取目录文件个数
	 * 
	 * @param f
	 * @return
	 */
	public static long getlist(File f) {
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}
	
	
	public static InputStream getResourceAsStream(String resource) {
		String stripped = resource.startsWith("/") ? resource.substring(1)
				: resource;

		InputStream stream = null;
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (classLoader != null) {
			stream = classLoader.getResourceAsStream(stripped);

		}
	
		return stream;
	}

}
