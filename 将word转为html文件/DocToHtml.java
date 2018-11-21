package com.leimingtech.core.util;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * @author: luohaiming
 * 
 * @version:word转html文件
 */
public class DocToHtml {

	public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static  void writeFile(String content, String path) {
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		try {
			File file = new File(path);
			fos = new FileOutputStream(file);
			bw = new BufferedWriter(new OutputStreamWriter(fos, "GB2312"));
			bw.write(content);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fos != null)
					fos.close();
			} catch (IOException ie) {
			}
		}
	}

	public static String convertToHtml(String fileName)
			throws TransformerException, IOException,
			ParserConfigurationException {
		String fileInName = PathFormat.parse("{time}{rand:6}","");
		String outPath = ResourceUtil.getCMSUploadFilesPath();
		String basedir=outPath+PathFormat.parse("/upload/image/htmlFile/{yyyy}/{mm}/{dd}","");
		File imageFolder = new File(basedir);
		if (!imageFolder.exists()) {
			imageFolder.mkdirs();
		}
		HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));
		// WordToHtmlUtils.loadDoc(new FileInputStream(inputFile));
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder()
						.newDocument());
		wordToHtmlConverter.setPicturesManager(new PicturesManager() {
			public String savePicture(byte[] content, PictureType pictureType,
					String suggestedName, float widthInches, float heightInches) {
				return suggestedName;
			}
		});
		wordToHtmlConverter.processDocument(wordDocument);
		// save pictures
		List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
		Map<String,String> m=new HashMap<String,String>();
		if (pics != null) {
			for (int i = 0; i < pics.size(); i++) {
				Picture pic = (Picture) pics.get(i);

				try {
					String newName = PathFormat.parse("{time}{rand:6}","")
							+ pic.suggestFullFileName();
					pic.writeImageContent(new FileOutputStream(imageFolder
							+ "//" + newName));
					m.put(pic.suggestFullFileName(), newName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		Document htmlDocument = wordToHtmlConverter.getDocument();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(out);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");
		serializer.transform(domSource, streamResult);
		out.close();
		String fileOutName =  basedir+"/"+fileInName+ ".html";
		String content = new String(out.toByteArray());
		for (String key : m.keySet()) {
			content=content.replaceAll("\""+key+"\"", "\""+m.get(key)+"\"");
		}
		
		writeFile(content, fileOutName);
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
