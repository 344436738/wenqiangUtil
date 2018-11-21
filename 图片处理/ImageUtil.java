package com.leimingtech.core.util.image;

import com.leimingtech.core.util.ResourceUtil;
import magick.MagickException;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;



public class ImageUtil {
	/**
	 * @param args
	 * @throws IOException
	 * @throws MagickException
	 */
	@Test
	public void test() throws IOException, MagickException {
		String s1="D:\\workspace\\47Shop\\UI\\html\\design\\sysDesign\\159\\2011\\06\\14\\211970\\banner.jpg";
		String s2="D:\\workspace\\47Shop\\UI\\html\\design\\sysDesign\\159\\2011\\06\\14\\211970\\banner2.jpg";
		ImageUtil.scaleRateImageFile(s1, s2, 500,400);
	}
	public static boolean existsImageMagick(){
		//这里从配置文件sysConfig.properties读
		if("1".equals(ResourceUtil.getImageUtilType())){
			return true;//调用imageMagickUtil
		}else{
			return false;//调用imageJDKutil
		}
	}
	
	public static Dimension getDimension(String fileName) throws MagickException, IOException {
		if(existsImageMagick()){
			return ImageMagickUtil.getDimension(fileName);
		}else{
			return ImageJDKUtil.getDimension(fileName);
		}
	}
	public static void scaleRateImageFile(String fromFileName, String toFileName,
			int toWidth, int toHeight) throws IOException, MagickException {
		if(existsImageMagick()){
			ImageMagickUtil.scaleRateImageFile(fromFileName, toFileName, toWidth, toHeight);
		}else{
			ImageJDKUtil.scaleRateImageFile(fromFileName, toFileName, toWidth, toHeight);
		}
	}
	public static boolean scaleRateImageFile2(String fromFileName, String toFileName,
			int toWidth, int toHeight){
		boolean flag=true;
		if(existsImageMagick()){
			flag=ImageMagickUtil.scaleRateImageFile2(fromFileName, toFileName, toWidth, toHeight);
			
		}else{
			try {
				ImageJDKUtil.scaleRateImageFile(fromFileName, toFileName, toWidth, toHeight);
				flag=true;
			} catch (IOException e) {
				flag=false;
				e.printStackTrace();
			}
		}
		return flag;
	}

	public static void scaleRateImageFile(String fromFileName, String toFileName, double rate) throws MagickException, IOException{
		if(existsImageMagick()){
			ImageMagickUtil.scaleRateImageFile(fromFileName, toFileName, rate);
		}else{
			ImageJDKUtil.scaleRateImageFile(fromFileName, toFileName, rate);
		}
	}

	public static void scaleFixedImageFile(String fromFileName, String toFileName,
			int toWidth, int toHeight) throws MagickException, IOException{
		if(existsImageMagick()){
			ImageMagickUtil.scaleFixedImageFile(fromFileName, toFileName, toWidth, toHeight);
		}else{
			ImageJDKUtil.scaleFixedImageFile(fromFileName, toFileName, toWidth, toHeight);
		}
	}

	public static void pressText(String filePath, String pressText,int color,
			int fontSize, int position) {
		if(existsImageMagick()){
			ImageMagickUtil.pressText(filePath, pressText, color, fontSize, position);
		}else{
			ImageJDKUtil.pressText(filePath, pressText, color, fontSize, position);
		}
	}

	public final static void pressImage(String targetImg, String pressImg,
			int position) {
		if(existsImageMagick()){
			ImageMagickUtil.pressImage(targetImg, pressImg, position);
		}else{
			ImageJDKUtil.pressImage(targetImg, pressImg, position);
		}
	}
	
	public final static void cutImage(String srcPath , String dirPath, String toWidth, String toHeight, String imgX1, String imgY1, double rate){
		if(rate<0){
			rate=0;
		}
		if(existsImageMagick()){
//			ImageMagickUtil.cutImg2(srcPath, dirPath, Integer.valueOf(imgWidth), Integer.valueOf(imgHeight), Integer.valueOf(imgX1), Integer.valueOf(imgY1));
			boolean isCut = ImageMagickUtil.cutImg(srcPath, dirPath, Double.valueOf(toWidth)*rate, Double.valueOf(toHeight)*rate, Double.valueOf(imgX1)*rate ,Double.valueOf(imgY1)*rate);
		}else{
			//jdk切图方法
			boolean isCut=ImageJDKUtil.cut(srcPath, dirPath, Integer.valueOf(toWidth)*rate, Integer.valueOf(toHeight)*rate, Integer.valueOf(imgX1)*rate, Integer.valueOf(imgY1)*rate);
		}
		
	}
	
//	public static boolean afterUploadImage(ZCImageSchema image,String absolutePath) throws NumberFormatException, Exception {
//		return afterUploadImage(image,absolutePath,null);
//	}
//	
//	public static boolean afterUploadImage(ZCImageSchema image, String absolutePath,Mapx fields ) throws NumberFormatException, Exception {
//		String imageFile =absolutePath + image.getSrcFileName();
//		boolean bakFlag = false;
//		Dimension dim = ImageUtil.getDimension(imageFile);
//		if(dim.getWidth()>=1600&&dim.getHeight()>=1200){
//			ImageUtil.scaleRateImageFile(imageFile, absolutePath+"b_"+ image.getFileName(), 1600, 1200);
//			imageFile = absolutePath+"b_"+ image.getFileName();
//			bakFlag = true;
//		}
//		image.setWidth((int)dim.getWidth());
//		image.setHeight((int)dim.getHeight());
//		// 系统缩略图，自动生成
//		ImageUtil.scaleRateImageFile(imageFile, absolutePath + "s_" +image.getFileName(), 120, 120);
//		
//		Mapx configFields = new Mapx();
//		configFields.putAll(ConfigImageLib.getImageLibConfig());
//		configFields.putAll(fields);
//		
////		 缩略图
//		int count = Integer.parseInt(configFields.get("Count").toString());
//		
//		for (int i = 1; i <= count; i++) {
//			if (configFields==null||"1".equals(configFields.get("HasAbbrImage"+i))) {
//				String SizeType = (String) configFields.get("SizeType"+i);
//				int Width = Integer.parseInt((String) configFields.get("Width"+i));
//				int Height = Integer.parseInt((String) configFields.get("Height"+i));
//				if ("1".equals(SizeType)) {
//					Height = 0;
//				} else if ("2".equals(SizeType)) {
//					Width = 0;
//				}
//				String abbrImage = absolutePath + i + "_" +image.getFileName();
//				// 固定大小
//				if("3".equals(SizeType)){
//					ImageUtil.scaleFixedImageFile(imageFile, abbrImage, Width, Height);
//				}else{
//					ImageUtil.scaleRateImageFile(imageFile, abbrImage, Width, Height);
//				}
//				image.setCount(image.getCount()+1);
//				if ("1".equals(configFields.get("HasWaterMark"+i))) {
//					if ("Image".equals(configFields.get("WaterMarkType"+i))) {
//						ImageUtil.pressImage(abbrImage, Config.getContextRealPath()
//								+ configFields.get("ImageFile"+i), Integer.parseInt(configFields.get("Position"+i)
//								.toString()));
//					} else {
//						ImageUtil.pressText(abbrImage, (String) configFields
//								.get("Text"+i), Integer.parseInt(configFields.get("FontColor"+i).toString()), Integer
//								.parseInt(configFields.get("FontSize"+i).toString()), Integer.parseInt(configFields.get(
//								"Position"+i).toString()));
//					}
//				}
//			}
//		}
//		
//		// 如果一个缩略图都没有生成，则拷贝一份原图作为缩略图
//		if(image.getCount()==0){
//			FileUtil.copyFile(new File(imageFile), absolutePath + "1_" +image.getFileName());
//			image.setCount(1);
//		}
//		
////		 给原图加水印
//		if (configFields==null||"1".equals(configFields.get("HasWaterMark"))) {
//			if ("Image".equals(configFields.get("WaterMarkType"))) {
//				ImageUtil.pressImage(absolutePath + image.getSrcFileName(), Config.getContextRealPath()
//						+ configFields.get("ImageFile"), Integer.parseInt(configFields.get("Position")
//						.toString()));
//			} else {
//				ImageUtil.pressText(absolutePath + image.getSrcFileName(), (String) configFields.get("Text"), Integer
//						.parseInt(configFields.get("FontColor").toString()), Integer.parseInt(configFields.get(
//						"FontSize").toString()), Integer.parseInt(configFields.get("Position").toString()));
//			}
//		}
//		
//		if(bakFlag){
//			new File(imageFile).delete();
//		}
//		return true;
//	}

}
