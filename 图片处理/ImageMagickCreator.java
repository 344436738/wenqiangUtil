package com.leimingtech.core.util.image;

import com.leimingtech.core.util.FileUtil;
import com.leimingtech.core.util.NumberUtil;
import magick.*;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;



@Service("IThumbnailCreator")
@Transactional
public class ImageMagickCreator implements IThumbnailCreator{

	/**
	 * @param args
	 * @throws IOException
	 * @throws MagickException
	 */
	@Test
	public void test() throws IOException, MagickException {
//		Map<String ,String> paramMap = new HashMap<String ,String>();
//		scaleRateImageFile("f:/1.jpg","f:/100x100.jpg",100,100);
//		scaleRateImageFile("f:/1.jpg","f:/100x0.jpg",100,0);
//		scaleRateImageFile("f:/1.jpg","f:/0x100.jpg",0,100);
//		scaleRateImageFile("f:/1.jpg","f:/1000x1000.jpg",1000,1000);
//		pressImage("F:/test/2.jpg", "E:/workspaceGBK/JShop2/UI/web/jcrop_zh/image/pingtu.png", 0, 0);
//		distortImg("F:/test/4.jpg", "F:/test/5.jpg", 90);
//		distortImg("F:/test/6.jpg", "F:/test/7.jpg", 90);
//		distortImg("F:/test/8.jpg", "F:/test/9.jpg", 90);
//		distortImg("F:/test/10.jpg", "F:/test/11.jpg", 90);
//		distortImg("F:/test/12.jpg", "F:/test/13.jpg", 90);
//		distortImg("F:/test/1.jpg", "F:/test/2.jpg", 90);
//		scaleRateImageFile("F:/test/6.jpg", "F:/test/7.jpg", 0,150);
//		IMGToProductManager.getCupPreviewImage("E:/workspaceGBK/JShop2/UI/html/design/userDesign/159/2013/04/18/16852/1902_13662737599295244_view.JPG", "E:/workspaceGBK/JShop2/UI/web/jcrop_zh/image/cup.jpg");
		// pressImage("E:/\1.jpg", "F:\\workspace\\MZB\\UI\\Upload\\sx\\Image\\WaterMarkImage.png", 9);
		// pressImage("E:\\1.jpg", "F:\\workspace\\MZB\\UI\\Upload\\sx\\Image\\WaterMarkImage.png", 8);
		// pressImage("E:\\1.jpg", "F:\\workspace\\MZB\\UI\\Upload\\sx\\Image\\WaterMarkImage.png", 7);
		// pressImage("E:\\1.jpg", "F:\\workspace\\MZB\\UI\\Upload\\sx\\Image\\WaterMarkImage.png", 6);
		// pressImage("E:\\1.jpg", "F:\\workspace\\MZB\\UI\\Upload\\sx\\Image\\WaterMarkImage.png", 5);
		// pressImage("E:\\1.jpg", "F:\\workspace\\MZB\\UI\\Upload\\sx\\Image\\WaterMarkImage.png", 4);
		// pressImage("E:\\1.jpg", "F:\\workspace\\MZB\\UI\\Upload\\sx\\Image\\WaterMarkImage.png", 3);
		// pressImage("E:\\1.jpg", "F:\\workspace\\MZB\\UI\\Upload\\sx\\Image\\WaterMarkImage.png", 2);
		// pressImage("E:\\1.jpg", "F:\\workspace\\MZB\\UI\\Upload\\sx\\Image\\WaterMarkImage.png", 1);
		// pressImage("E:\\1.jpg", "F:\\workspace\\MZB\\UI\\Upload\\sx\\Image\\WaterMarkImage.png", 0);
		// PressText("E:\\1.jpg", "民政部", "", "宋体", 1, 1, 1, 1);
		//scaleRateImageFile("i://test.bmp", "i:\\test1.bmp", 200, 150);
//		convertCMKYtoRGB("D:/CMYK.jpg","d:/abc.jpg");
//		qualityImg("D:/test/DSC02182.JPG","D:/test/DSC02182.JPG","75");
//		scaleRateImageFile("D:/uwp/FFJ_1370A.JPG", "D:/uwp/FFJ_1370A1.JPG", 200, 150);
//		compression("D:/uwp/FFJ_1370A.JPG",75,"D:/uwp/FFJ_1370A.JPG");
//	    File file=new File("E:/eclipse/workspace/Shop/UI/html/design/userDesign/159/2011/12/16/2338/yzhc/");
//	    File[] fils=file.listFiles();
//	    for(int i=0;i<fils.length;i++){
//	    	File sf=fils[i];
//	    	if(!(sf.getName().endsWith(".xml"))){
//	    	qualityImg(sf.getPath(),sf.getPath());
//	    	}
//	    }
		
		ImageMagickCreator imageMagickCreator = new ImageMagickCreator();
		imageMagickCreator.pressText("E:\\1.jpg","omg",0,0,0);
		
//		ImageMagickUtil.scaleFixedImageFileWhite("D:/324x270.jpg", "D:/324x27001.jpg", 324, 400);
//		ImageMagickUtil.scaleFixedImageFileWhite("D:/324x27001.jpg", "D:/324x27002.jpg", 400, 400);
		//原图  , 水印  
		/*ImageMagickCreator.pressImage("D:/aaaa.jpg","D:/qqqq.png" , 0,0);
		
		// 压缩图String imagePath,int quality,String newImagePath
		ImageMagickCreator.compression("D:/222.jpg", 75, "D:/2333.jpg");*/
		 System.exit(0);
	}
	
	
	public ImageMagickCreator() {
		
	}
	
	
	public static void convertCMKYtoRGB(String fromFileName, String toFileName)throws MagickException {
		System.setProperty("jmagick.systemclassloader", "no");
		System.out.print("正在转换CMYK to RGB--" + toFileName);

		MagickImage fromImage = null;
		try {
			ImageInfo info = new ImageInfo(fromFileName);
			int Colorspace=info.getColorspace();
			System.out.print("Colorspace--" + Colorspace);
			fromImage = new MagickImage(info);
			

		} finally {
			if (fromImage != null) {
				fromImage.destroyImages();
			}
		}
		System.out.println("完毕");
    }
	
//	public static void qualityImg(String fromFileName, String toFileName,String q)throws MagickException {
//		System.out.print("正在转换压缩质量--" + toFileName);
//
//		Process child=null;
//		try{
////			ImageInfo info = new ImageInfo(fromFileName);
////			ImageInfo info2 = new ImageInfo(toFileName);
////			System.out.println(info.getColorspace());
////			System.out.println(info2.getColorspace());
////			child= Runtime.getRuntime().exec("convert +profile '*' -quality "+q+" "+fromFileName+" "+toFileName);
//			VideoUtil.exec("convert +profile '*' -quality "+q+" "+fromFileName+" "+toFileName);
//			while(child.waitFor()!=0){
//				System.out.println(child.waitFor());
//				InputStream in = child.getErrorStream();
//				if(in.read()>0){
//					throw new Exception("转换压缩质量错误");
//				}
//				Thread.sleep(100);
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		System.out.println("完毕");
//    }
	
	/**
	 * 压缩图片
	 */
    public  static boolean compression(String imagePath,int quality,String newImagePath){
    	System.setProperty("jmagick.systemclassloader","no");
    	        //创建imageInfo对象
    	        ImageInfo imageInfo;
    	        MagickImage image = null;
				try {
					imageInfo = new ImageInfo(imagePath);

	    	        //设置压缩比例
	   	        imageInfo.setQuality(quality);
	   	        //读取imageInfo
	    	        image = new MagickImage(imageInfo);
	    	        //设置新图片的路径
	    	        image.setFileName(newImagePath);
	    	        //执行
	    	        image.writeImage(imageInfo);
	   	        //销毁
	    	        return true;
				} catch (MagickException e) {
					e.printStackTrace();
					System.out.println("图片格式转换错误！");
					return false;
				} finally {
					if (image != null) {
						image.destroyImages();
					}
				}
    	}
    
	public static void qualityImg(String fromFileName, String toFileName)throws MagickException {
		compression(fromFileName, 75, toFileName);
//		ImageMagickUtil.qualityImg(fromFileName, toFileName,"75");
    }
	
	/**
	 * 获得图片属性
	 * @param fileName
	 * @return
	 * @throws MagickException
	 */
	public Dimension getDimension(String fileName) {
		System.setProperty("jmagick.systemclassloader", "no");
		ImageInfo info = null;
		MagickImage fromImage = null;
		Dimension dim = null;
			try {
				info = new ImageInfo(fileName);
				fromImage = new MagickImage(info);
				dim = fromImage.getDimension();
			} catch (MagickException e) {
				e.printStackTrace();
			}finally {
			if (fromImage != null) {
				fromImage.destroyImages();
			}
		}
		return dim ;
	}
	public static void rotateImage(String fromFileName, String toFileName,int rotate) throws MagickException {
		rotateImage(fromFileName, toFileName, rotate*1.0);
	}
	public static void rotateImage(String fromFileName, String toFileName,double rotate) throws MagickException {
		System.setProperty("jmagick.systemclassloader", "no");
		MagickImage fromImage = null;
		MagickImage toImage = null;
		try {
			ImageInfo info = new ImageInfo(fromFileName);
			fromImage = new MagickImage(info);
			MagickImage rotateImage = fromImage.rotateImage(rotate);
			rotateImage.setFileName(toFileName);// 设置输出的文件名
			rotateImage.writeImage(info); // 保存
		} finally {
			if (fromImage != null) {
				fromImage.destroyImages();
			}
		}
	}

	public void scaleRateImageFile(String fromFileName, String toFileName, int toWidth, int toHeight){
		System.setProperty("jmagick.systemclassloader", "no");
		ImageInfo info = null;
		MagickImage fromImage = null;
		try {
			info = new ImageInfo(fromFileName);

			fromImage = new MagickImage(info);
			Dimension dim = fromImage.getDimension();
			double w = dim.getWidth();
			double h = dim.getHeight();
			if (w < toWidth && h < toHeight) {
				FileUtil.copyFile(new File(fromFileName), toFileName);
				return ;
			}
			if (toWidth == 0) {
				scaleRateImageFile(fromFileName, toFileName, toHeight / h);
				return;
			} else if (toHeight == 0) {
				scaleRateImageFile(fromFileName, toFileName, toWidth / w);
				return;
			}
			if (toWidth / w > toHeight / h) {
				scaleRateImageFile(fromFileName, toFileName, toHeight / h);
			} else {
				scaleRateImageFile(fromFileName, toFileName, toWidth / w);
			}
		}catch (MagickException e) {
			e.printStackTrace();
		} finally {
			if (fromImage != null) {
				fromImage.destroyImages();
			}
		}
	}

	public void scaleRateImageFile(String fromFileName, String toFileName, double rate) {
		System.out.print("ImageMagickUtil正在生成缩略图-scaleRateImageFile--" + toFileName);
		System.setProperty("jmagick.systemclassloader", "no");
		MagickImage fromImage = null;
		try {
			ImageInfo info = new ImageInfo(fromFileName);
			fromImage = new MagickImage(info);
			Dimension dim = fromImage.getDimension();
			double w = dim.getWidth();
			double h = dim.getHeight();
			MagickImage toImage = fromImage.scaleImage((int) (w * rate), (int) (h * rate));// 缩放操作
			toImage.setFileName(toFileName);// 设置输出的文件名
			toImage.writeImage(new ImageInfo(fromImage.getFileName())); // 保存
		} catch (MagickException e) {
			e.printStackTrace();
		}finally {
			if (fromImage != null) {
				fromImage.destroyImages();
			}
		}
		System.out.println("完毕");
	}
	public boolean scaleRateImageFile2(String fromFileName, String toFileName, int toWidth, int toHeight)
	{
		System.setProperty("jmagick.systemclassloader", "no");
		ImageInfo info = null;
		MagickImage fromImage = null;
		boolean flag=true;
		try {
			Dimension dim = null;
			try {
				info = new ImageInfo(fromFileName);
				fromImage = new MagickImage(info);
				 dim = fromImage.getDimension();
			} catch (MagickException e) {
				flag=false;
				e.printStackTrace();
			}
			double w = dim.getWidth();
			double h = dim.getHeight();
			if (w < toWidth && h < toHeight) {
				FileUtil.copyFile(new File(fromFileName), toFileName);
				return flag;
			}
			if (toWidth == 0) {
				boolean b=scaleRateImageFile2(fromFileName, toFileName, toHeight / h);
				if(b ==true){
					return flag;
				}else{
					flag=false;
					return flag;
				}
			} else if (toHeight == 0) {
				boolean b= scaleRateImageFile2(fromFileName, toFileName, toWidth / w);
				if(b ==true){
					return flag;
				}else{
					flag=false;
					return flag;
				}
			}
			if (toWidth / w > toHeight / h) {
				boolean b=scaleRateImageFile2(fromFileName, toFileName, toHeight / h);
				if(b ==false){
					flag=false;
				}
			} else {
				boolean b=scaleRateImageFile2(fromFileName, toFileName, toWidth / w);
				if(b ==false){
					flag=false;
				}
			}
		} finally {
			if (fromImage != null) {
				fromImage.destroyImages();
			}
		}
		try {
			qualityImg(toFileName,toFileName);
		} catch (Exception e) {
			System.out.println("压缩图片质量错误》》》》》"+toFileName);
//			e.printStackTrace();
		}
		 return flag;
	}
	public static boolean scaleRateImageFile2(String fromFileName, String toFileName, double rate){
		System.out.print("ImageMagickUtil正在生成缩略图-scaleRateImageFile--" + toFileName);
		System.setProperty("jmagick.systemclassloader", "no");
		MagickImage fromImage = null;
		boolean flag=true;
		try {
			try {
				ImageInfo info = new ImageInfo(fromFileName);
				fromImage = new MagickImage(info);
				Dimension dim = fromImage.getDimension();
				double w = dim.getWidth();
				double h = dim.getHeight();
				MagickImage toImage = fromImage.scaleImage((int) (w * rate), (int) (h * rate));// 缩放操作
				toImage.setFileName(toFileName);// 设置输出的文件名
				toImage.writeImage(new ImageInfo(fromImage.getFileName())); // 保存
			} finally {
				if (fromImage != null) {
					fromImage.destroyImages();
				}
			}
		} catch (MagickException e) {
			flag=false;
			e.printStackTrace();
		}
		System.out.println("缩图完毕");
		return flag;
	}
	public void scaleFixedImageFile(String fromFileName, String toFileName, int toWidth, int toHeight) {
		System.out.print("ImageMagickUtil正在生成缩略图-scaleFixedImageFile--" + toFileName);
		System.setProperty("jmagick.systemclassloader", "no");// 这个没什么好说的，照办就是了
		MagickImage fromImage = null;
		MagickImage toImage = null;
		try {
			ImageInfo info = new ImageInfo(fromFileName);
			fromImage = new MagickImage(info);
			toImage = fromImage.scaleImage(toWidth, toHeight);// 缩放操作
			toImage.setFileName(toFileName);// 设置输出的文件名
			toImage.writeImage(info); // 保存
		} catch (MagickException e) {
			e.printStackTrace();
		}finally {
			if (fromImage != null) {
				fromImage.destroyImages();
			}
			if (toImage != null) {
				toImage.destroyImages();
			}
		}
		System.out.println("完毕");
	}
	
	/**
	 * 缩放成固定大小，不足的补白
	 * @param fromFileName
	 * @param toFileName
	 * @param toWidth
	 * @param toHeight
	 * @throws MagickException
	 */
	public static void scaleFixedImageFileWhite(String fromFileName, String toFileName, int w, int h)
	throws MagickException {
		int target_w, target_h; // 目标宽高
		int x = 0, y = 0; // 缩略图在背景的座标
		target_w = w;
		target_h = h;
		
		System.out.print("ImageMagickUtil正在生成缩略图-scaleFixedImageFileWhite--" + toFileName);
		System.setProperty("jmagick.systemclassloader", "no");
		MagickImage fromImage = null;
		MagickImage toImage = null;
		try {
			ImageInfo info = new ImageInfo(fromFileName);
			fromImage = new MagickImage(info);
			Dimension dim = fromImage.getDimension();
			double width = dim.getWidth();
			double height = dim.getHeight();
			/* 计算目标宽高 */
			if (width / height > w / h) { // 原图长:上下补白
				target_w = w;
				target_h = (int) (target_w * height / width);
				x = 0;
				y = (int) (h - target_h) / 2;

			}

			if (width / height < w / h) { // 原图高:左右补白
				target_h = h;
				target_w = (int) (target_h * width / height);
				y = 0;
				x = (int) (w - target_w) / 2;
			}
			MagickImage thumb_img = fromImage.scaleImage(target_w, target_h);
			MagickImage blankImage = new MagickImage();

			byte[] pixels = new byte[w * h * 4];
			for (int i = 0; i < w * h; i++) {
				pixels[4 * i] = (byte) 231;
				pixels[4 * i + 1] = (byte) 232;
				pixels[4 * i + 2] = (byte) 224;
				pixels[4 * i + 3] = (byte) 255;
			}

			blankImage.constituteImage(w, h, "RGBA", pixels);
			blankImage.compositeImage(CompositeOperator.AtopCompositeOp,
					thumb_img, x, y);
			blankImage.setFileName(toFileName);
			blankImage.writeImage(info);

		} finally {
			if (fromImage != null) {
				fromImage.destroyImages();
			}
			if (toImage != null) {
				toImage.destroyImages();
			}
		}
		 

	}

	public void pressText(String filePath, String pressText, int color, int fontSize, int position) {
		System.out.print("ImageMagickUtil正在打文字水印--" + filePath);
		System.setProperty("jmagick.systemclassloader", "no");
		MagickImage image = null;
		try {
			ImageInfo info = new ImageInfo(filePath);

			if (filePath.toUpperCase().endsWith("JPG") || filePath.toUpperCase().endsWith("JPEG")) {
				info.setCompression(CompressionType.JPEGCompression); // 压缩类别为JPEG格式
				info.setPreviewType(PreviewType.JPEGPreview); // 预览格式为JPEG格式
				info.setQuality(95);
			}

			image = new MagickImage(info);
			Dimension dim = image.getDimension();
			int wideth = (int) dim.getWidth();
			int height = (int) dim.getHeight();
			if (wideth <= 300 && height <= 300) {
				return;
			}
			DrawInfo drawInfo = new DrawInfo(info);

			drawInfo.setFill(PixelPacket.queryColorDatabase("white"));
			// drawInfo.setUnderColor(PixelPacket.queryColorDatabase("white"));
			drawInfo.setOpacity(0);
			drawInfo.setPointsize(20);

			// drawInfo.setFont("Arial"); // 英文使用此字体也可

			// // 注意这里解决中文字体问题，有以下两行才可正常显示
			String fontPath = "C:/WINDOWS/Fonts/SIMSUN.TTC";
			// String fontPath = "SIMSUN.TTC";
			drawInfo.setFont(fontPath);
			drawInfo.setTextAntialias(true);

			// Step 3: Writing The Text
			drawInfo.setText(pressText);
			drawInfo.setGeometry("+1500+1000");

			// Step 4: Annotating
			image.annotateImage(drawInfo);

			// Step 5: Writing the new file
			image.setFileName(filePath);
			image.writeImage(info);
			image.destroyImages();
			image = null;

		} catch (MagickException e) {
			e.printStackTrace();
		} finally {
			if (image != null) {
				image.destroyImages();
			}
		}
		System.out.println("完毕");
	}

	public final void pressImage(String targetImg, String pressImg, int position) {
		System.out.print("ImageMagickUtil正在打图片水印--" + targetImg);
		System.setProperty("jmagick.systemclassloader", "no");
		MagickImage image = null;
		MagickImage press = null;
		try {
			ImageInfo imageInfo = new ImageInfo(targetImg);
			image = new MagickImage(imageInfo);
			Dimension dim = image.getDimension();
			int wideth = (int) dim.getWidth();
			int height = (int) dim.getHeight();
			if (wideth <= 300 && height <= 300) {
				return;
			}
			press = new MagickImage(new ImageInfo(pressImg));
			dim = press.getDimension();
			int wideth_press = (int) dim.getWidth();
			int height_press = (int) dim.getHeight();
			int x = 0, y = 0;
			int bianju = 20;
			int[][][] positions = new int[][][] {
					{ { bianju, bianju }, { (wideth - wideth_press) / 2, bianju },
							{ wideth - wideth_press - bianju, bianju } },
					{ { bianju, (height - height_press) / 2 },
							{ (wideth - wideth_press) / 2, (height - height_press) / 2 },
							{ wideth - wideth_press - bianju, (height - height_press) / 2 } },
					{ { bianju, height - height_press - bianju },
							{ (wideth - wideth_press) / 2, height - height_press - bianju },
							{ wideth - wideth_press - bianju, height - height_press - bianju } } };
			if (position == 0) {
				position = NumberUtil.getRandomInt(9)+1;
			}
			x = positions[(position - 1) / 3][(position - 1) % 3][0];
			y = positions[(position - 1) / 3][(position - 1) % 3][1];

			image.compositeImage(CompositeOperator.AtopCompositeOp, press, x, y);
			image.setFileName(targetImg);
			image.writeImage(imageInfo);
		} catch (MagickException e) {
			e.printStackTrace();
		} finally {
			if (image != null) {
				image.destroyImages();
			}
			if (press != null) {
				press.destroyImages();
			}
		}
		System.out.println("完毕");
	}
	
	/**
	 *打水印 
	 */
	public final static void pressImage(String targetImg, String pressImg, int x, int y) {
		System.out.print("ImageMagickUtil正在打图片水印--" + targetImg);
		System.setProperty("jmagick.systemclassloader", "no");
		MagickImage image = null;
		MagickImage press = null;
		try {
			ImageInfo imageInfo = new ImageInfo(targetImg);
			image = new MagickImage(imageInfo);
			press = new MagickImage(new ImageInfo(pressImg));
			image.compositeImage(CompositeOperator.AtopCompositeOp, press, x, y);
			image.setFileName(targetImg);
			image.writeImage(imageInfo);
		} catch (MagickException e) {
			e.printStackTrace();
		} finally {
			if (image != null) {
				image.destroyImages();
			}
			if (press != null) {
				press.destroyImages();
			}
		}
		System.out.println("完毕");
	}
	/**
	 *  CMYK转RGB
	 */
    public static void cmyktorgb(String  newFilePath){
    	Process child=null;
		BufferedImage br=null;
		try{
			br =ImageIO.read(new File(newFilePath));
		}catch(Exception e){
			System.out.println("文件为CMYK图片，现在开始转换");
			try{
				child= Runtime.getRuntime().exec("D:/Program Files/ImageMagick-6.3.9-Q8/convert +profile '*' -colorspace RGB "+newFilePath+" "+newFilePath);
				int i = 0;
				while(child.waitFor()!=0&&i<3000){
					System.out.println(child.waitFor());
					InputStream in = child.getErrorStream();
					if(in.read()>0){
						throw new Exception("CMYK TO RGB 错误！");
					}
					Thread.sleep(100);
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
    }
    /**
     * 按照指定位置切图
     */
    public static boolean  cutImg(String imgPath,String newimgPath,double width,double height,double x,double y){
    	Process child=null;
    	boolean flag=true;
    	try{
    	child= Runtime.getRuntime().exec("D:/Program Files/ImageMagick-6.3.9-Q8/convert -crop "+width+"x"+height+"+"+x+"+"+y+" "+imgPath+" "+newimgPath);
		int i = 0;
    	while(child.waitFor()!=0&&i++<3000){
			System.out.println(child.waitFor());
			InputStream in = child.getErrorStream();
			if(in.read()>0){
//				throw new Exception("CMYK TO RGB 错误！");
				flag=false;
			}
			Thread.sleep(100);
		}
		}catch(Exception e1){
			e1.printStackTrace();
			flag=false;
		}
		return flag;
    }
    
    /** 
     * 切图 
     * ＠param imgPath 源图路径 
     * ＠param toPath  批改图路径 
     * ＠param w 
     * ＠param h 
     * ＠param x 
     * ＠param y 
     * ＠throws MagickException 
     */  
    public static boolean cutImg2(String imgPath, String toPath, double w, double h, double x, double y) throws MagickException {
    	return cutImg2(imgPath, toPath, Math.round(w), Math.round(h), Math.round(x), Math.round(y));
    }
    public static boolean cutImg2(String imgPath, String toPath, float w, float h, float x, float y) throws MagickException {
    	return cutImg2(imgPath, toPath, Math.round(w), Math.round(h), Math.round(x), Math.round(y));
    }
    public static boolean cutImg2(String imgPath, String toPath, int w, int h, int x, int y) throws MagickException {  
        ImageInfo infoS = null;  
        MagickImage image = null;  
        MagickImage cropped = null;  
        Rectangle rect = null;  
        try {
            infoS = new ImageInfo(imgPath);  
            image = new MagickImage(infoS);  
            rect = new Rectangle(x, y, w, h);  
            cropped = image.cropImage(rect);  
            cropped.setFileName(toPath);  
            cropped.writeImage(infoS);     
        } finally {  
            if (cropped != null) {  
                cropped.destroyImages();  
            }  
        }
        return true;
    }  
    
    /**
     * 扭曲图片
     * @param imgPath
     * @param newimgPath
     * @param jiaodu
     * @param xuanzhuan
     * @return
     */
    public static boolean  distortImg(String imgPath,String newimgPath,int jiaodu){
    	return distortImg(imgPath, newimgPath, (double)jiaodu, 0);
    }
    public static boolean  distortImg(String imgPath,String newimgPath,int jiaodu,int xuanzhuan){
    	return distortImg(imgPath, newimgPath, (double)jiaodu, (double)xuanzhuan);
    }
    public static boolean  distortImg(String imgPath,String newimgPath,double jiaodu){
    	return distortImg(imgPath, newimgPath, jiaodu, 0);
    }
    public static boolean  distortImg(String imgPath,String newimgPath,double jiaodu,double xuanzhuan){
    	Process child=null;
    	boolean flag=true;
    	try{
    	child= Runtime.getRuntime().exec("D:/Program Files/ImageMagick-6.3.9-Q8/convert "+imgPath+" -distort arc \""+jiaodu+" "+xuanzhuan+"\" "+ newimgPath);
		int i = 0;
    	while(child.waitFor()!=0&&i++<3000){
			System.out.println(child.waitFor());
			InputStream in = child.getErrorStream();
			if(in.read()>0){
//				throw new Exception("CMYK TO RGB 错误！");
				flag=false;
			}
			Thread.sleep(100);
		}
		}catch(Exception e1){
			e1.printStackTrace();
			flag=false;
		}
		return flag;
    }
	@Override
	public boolean cutImage(String srcPath, String dirPath, String toWidth,
			String toHeight, String imgX1, String imgY1, double rate) {
		boolean isCut = cutImg(srcPath, dirPath, Double.valueOf(toWidth)*rate, Double.valueOf(toHeight)*rate, Double.valueOf(imgX1)*rate ,Double.valueOf(imgY1)*rate);
		return isCut;
	}

}
