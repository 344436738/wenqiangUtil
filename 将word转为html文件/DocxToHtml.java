package com.leimingtech.core.util;

import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.util.Random;



/**
 * 将word转为html文件
 *
 * @author luohaiming
 *
 */
public class DocxToHtml {
	public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 *
	 * @param wordName word文件路径
	 * @return
	 * @throws IOException
	 */
	public static String doGenerateSysOut(String wordName) throws IOException {
		String fileInName = PathFormat.parse("{time}{rand:6}","");
		String outPath = ResourceUtil.getCMSUploadFilesPath();
		String basedir=outPath+PathFormat.parse("/upload/image/htmlFile/{yyyy}/{mm}/{dd}","");
		File dirFile=new File(basedir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File imageFolder = new File(basedir+"/"+fileInName);

		String fileOutName =  basedir+"/"+fileInName+ ".html";
		XWPFDocument document = new XWPFDocument(new FileInputStream(wordName));
		XHTMLOptions options = XHTMLOptions.create().indent(4);
		// Extract image
		options.setExtractor(new FileImageExtractor(imageFolder));
		// URI resolver
		options.URIResolver(new FileURIResolver(imageFolder));
		File outFile = new File(fileOutName);
		OutputStream out = new FileOutputStream(outFile);
		XHTMLConverter.getInstance().convert(document, out, options);
		return fileOutName;
	}

	public static String generateString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
		}
		return sb.toString();
	}
}