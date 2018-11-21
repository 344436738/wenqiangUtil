package com.leimingtech.cms.net.img;

import com.leimingtech.core.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/***
 * 抓取网络图片
 * 
 * @company 雷铭智信
 * @author 谢进伟
 * @DateTime 2015-7-30 下午2:57:32
 * 
 */
public class CatchNetworkImageUtil {
	
	/**
	 * 编码
	 */
	private static final String ECODING = "UTF-8";
	/**
	 * 获取img标签正则
	 */
	private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
	/**
	 * 获取src路径的正则
	 */
	private static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";
	
	/**
	 * 抓取指定url网页所有图片
	 * 
	 * @param url
	 *            地址
	 * @param saveFolder
	 *            抓取后图片存储目录
	 * @throws Exception
	 */
	public static void catchAllImgByURL(String url , String saveFolder) throws Exception {
		// 获得html文本内容
		String HTML = getHTML(url);
		// 获取图片标签
		List<String> imgUrl = getImageUrl(HTML);
		// 获取图片src地址
		List<String> imgSrcs = getImageSrc(imgUrl);
		// 下载图片
		Download(imgSrcs , saveFolder);
	}
	
	/**
	 * 抓取指定html内容的所有图片
	 * 
	 * @param html
	 *            内容
	 * @param saveFolder
	 *            抓取后图片存储目录
	 * @throws Exception
	 */
	public static void catchAllImgByHTML(String html , String saveFolder) throws Exception {
		// 获取图片标签
		List<String> imgUrl = getImageUrl(html);
		// 获取图片src地址
		List<String> imgSrcs = getImageSrc(imgUrl);
		// 下载图片
		Download(imgSrcs , saveFolder);
	}
	
	/***
	 * 获取HTML内容
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private static String getHTML(String url) throws Exception {
		URL uri = new URL(url);
		URLConnection uc = uri.openConnection();
		uc.setRequestProperty("User-Agent" , "Mozilla/4.0 (compatible; MSIE 5.0; Windows XP; DigExt)");
		InputStream in = uc.getInputStream();
		byte [] buf = new byte [1024];
		StringBuffer sb = new StringBuffer();
		while (in.read(buf , 0 , buf.length) > 0) {
			sb.append(new String(buf , ECODING));
		}
		in.close();
		return sb.toString();
	}
	
	/***
	 * 获取ImageUrl地址
	 * 
	 * @param HTML
	 * @return
	 */
	public static List<String> getImageUrl(String HTML) {
		Matcher matcher = Pattern.compile(IMGURL_REG).matcher(HTML);
		List<String> listImgUrl = new ArrayList<String>();
		while (matcher.find()) {
			listImgUrl.add(matcher.group());
		}
		return listImgUrl;
	}
	
	/***
	 * 获取ImageSrc地址
	 * 
	 * @param listImageUrl
	 * @return
	 */
	public static List<String> getImageSrc(List<String> listImageUrl) {
		List<String> listImgSrc = new ArrayList<String>();
		for(String image : listImageUrl) {
			Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
			while (matcher.find()) {
				listImgSrc.add(matcher.group().substring(0 , matcher.group().length() - 1));
			}
		}
		return listImgSrc;
	}
	
	/***
	 * 下载图片
	 * 
	 * @param listImgSrc
	 * @param saveFolder
	 */
	private static void Download(List<String> listImgSrc , String saveFolder) {
		try {
			File saveFolderFile = new File(saveFolder);
			if(!saveFolderFile.exists()) {
				saveFolderFile.mkdirs();
			}
			for(String url : listImgSrc) {
				String imageName = url.substring(url.lastIndexOf("/") + 1 , url.length());
				URL uri = new URL(url);
				InputStream in = uri.openStream();
				FileOutputStream fo = new FileOutputStream(new File(saveFolderFile.getPath() + "/" + formatFileName(imageName)));
				byte [] buf = new byte [1024];
				int length = 0;
				System.out.println("开始下载:" + url);
				while ((length = in.read(buf , 0 , buf.length)) != -1) {
					fo.write(buf , 0 , length);
				}
				in.close();
				fo.close();
				System.out.println(imageName + "下载完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("下载失败");
		}
	}
	
	/**
	 * 格式化文件名称,去掉特殊字符
	 * 
	 * @param fileName
	 *            需要格式化的文件名称
	 * @return
	 */
	private static String formatFileName(String fileName) {
		if(!StringUtils.isEmpty(fileName)) {
			fileName = StringUtils.replaceEach(fileName , new String []{"\\" , "/" , "*" , ">" , "<" , ":" , "|" , "?" , "\""} , new String []{"_" , "_" , "_" , "_" , "_" , "_" , "_" , "_" , "_"});
		}
		return fileName;
	}
}