package com.leimingtech.core.util.image;

import com.keypoint.PngEncoder;
import com.leimingtech.core.util.FileUtil;
import com.leimingtech.core.util.NumberUtil;
import org.junit.Test;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public class ImageJDKCreator implements IThumbnailCreator{


	public ImageJDKCreator() {
		
	}
	/**
	 * 得到一个Image对象按比例缩放后的新的Image对象
	 * 
	 * @param srcImage
	 * @param rate
	 * @return 返回缩放之后的Image对象
	 */
	public static BufferedImage scaleRate(BufferedImage srcImage, double rate) {
		return scaleRate(srcImage, rate, rate, null);
	}
	
	/**
	 * 缩放图像，使宽不超过width，高不超过height，同时保持比例不变
	 * 
	 * @param srcImage
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage scaleRate(BufferedImage srcImage, int width, int height) {
		double w = srcImage.getWidth();
		double h = srcImage.getHeight();
		if (w < width && h < height) {
			return srcImage;
		}
		if (height == 0) {
			if (w <= width) {
				return srcImage;
			} else {
				return scaleRate(srcImage, width / w, width / w, null);
			}
		} else if (width == 0) {
			if (h <= height) {
				return srcImage;
			} else {
				return scaleRate(srcImage, height / h, height / h, null);
			}
		}
		if (w / h > width / height) {
			return scaleRate(srcImage, width / w, width / w, null);
		} else {
			return scaleRate(srcImage, height / h, height / h, null);
		}
	}

	/**
	 * 图像灰度化
	 * 
	 * @param srcImage
	 * @return
	 */
	public static BufferedImage gray(BufferedImage srcImage) {
		BufferedImage dstImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), srcImage.getType());
		Graphics2D g2 = dstImage.createGraphics();
		RenderingHints hints = g2.getRenderingHints();
		g2.dispose();
		ColorSpace grayCS = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp colorConvertOp = new ColorConvertOp(grayCS, hints);
		colorConvertOp.filter(srcImage, dstImage);
		return dstImage;
	}

	/**
	 * 横纵向分别按指定比例缩放
	 * 
	 * @param srcImage
	 * @param xscale
	 * @param yscale
	 * @param hints
	 * @return
	 */
	public static BufferedImage scaleRate(BufferedImage srcImage, double xscale, double yscale, RenderingHints hints) {
		AffineTransform transform = AffineTransform.getScaleInstance(xscale, yscale);
		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(srcImage, null);
	}

	/**
	 * 缩入图片至固定大小
	 */
	public static BufferedImage scaleFixed(BufferedImage srcImage, int width, int height, boolean keepRate) {
		int srcWidth = srcImage.getWidth(); // 源图宽度
		int srcHeight = srcImage.getHeight(); // 源图高度
		double wScale = width * 1.0 / srcWidth;
		double hScale = height * 1.0 / srcHeight;
		if (keepRate) {
			if (wScale > hScale && hScale != 0) {
				wScale = hScale;
			} else {
				hScale = wScale;
			}
		}
		ImageScale is = new ImageScale();
		return is.doScale(srcImage, (int) (srcWidth * wScale), (int) (srcHeight * hScale));
	}

	public void scaleFixedImageFile(String srcFile, String destFile, int width, int height){
		scaleFixedImageFile(srcFile, destFile, width, height, true);
	}

	/**
	 * 将图像文件srcFile缩放至固定大小后写到destFile
	 * 
	 * @param srcFile
	 * @param destFile
	 * @param width
	 * @param height
	 * @param keepRate
	 * @throws IOException
	 */
	public void scaleFixedImageFile(String srcFile, String destFile, int width, int height, boolean keepRate){
		try {
			if (srcFile.toLowerCase().endsWith(".gif")) {// 要特别处理，JDK不能处理透明GIF
				BufferedImage image = readImage(srcFile);
				int srcWidth = image.getWidth(); // 源图宽度
				int srcHeight = image.getHeight(); // 源图高度
				if(srcWidth<=width&&srcHeight<=height){
					FileUtil.copy(srcFile, destFile);
					return;
				}
				OutputStream os = new FileOutputStream(destFile);
				try {
					GifUtil.resizeByRate(srcFile, os, width, height, keepRate);
				} catch (Exception e) {
					e.printStackTrace();
				}
				os.close();
			} else {
				BufferedImage image = readImage(srcFile);
				int srcWidth = image.getWidth(); // 源图宽度
				int srcHeight = image.getHeight(); // 源图高度
				if(srcWidth<=width&&srcHeight<=height){
					FileUtil.copy(srcFile, destFile);
					return;
				}
				BufferedImage newImage = scaleFixed(image, width, height, keepRate);
				writeImageFile(destFile, newImage);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 本方法中读取后重写到另一个BufferedImage，在一些情况下会极大地提高性能。
	 */
	public static BufferedImage readImage(String srcFile) throws IOException {
		BufferedImage src = null;
		if (srcFile.toLowerCase().endsWith(".bmp")) {
			src = BmpUtil.read(srcFile);
		} else {
			src = ImageIO.read(new File(srcFile));
		}
		BufferedImage image = new BufferedImage(src.getWidth(),src.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = image.createGraphics();
		g.drawImage(src, 0, 0, src.getWidth(),src.getHeight(), null);
		return image;
	}

	/**
	 * 将图像文件srcFile缩放后写到destFile，保持图片比例不变，但宽不超过width,高不超过height
	 * 
	 * @param fileName
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	public void scaleRateImageFile(String srcFile, String destFile, int width, int height){
		scaleFixedImageFile(srcFile, destFile, width, height, true);
	}

	/**
	 * 按比例将图像文件srcFile缩放后写到destFile
	 * 
	 * @param fileName
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	public void scaleRateImageFile(String srcFile, String destFile, double rate){
		try {
			if (srcFile.toLowerCase().endsWith(".gif")) {// 要特别处理，JDK不能处理透明GIF
				OutputStream os = new FileOutputStream(destFile);
				try {
					GifUtil.resizeByRate(srcFile, os, rate, rate);
				} catch (Exception e) {
					e.printStackTrace();
				}
				os.close();
			} else {
				BufferedImage image = readImage(srcFile);
				BufferedImage newImage = scaleRate(image, rate);
				writeImageFile(destFile, newImage);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将图像文件srcFile灰度化后写到destFile
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public static void grayImageFile(String srcFile, String destFile) throws IOException {
		writeImageFile(destFile, gray(ImageIO.read(new File(srcFile))));
	}

	public static void writeImageFile(String fileName, BufferedImage image) throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		if (fileName.toLowerCase().endsWith(".gif")) {
			GIFEncoder gif = new GIFEncoder(image, fos);
			gif.encode();
		}
		if (fileName.toLowerCase().endsWith(".png")) {
			//ImageIO.write(image, "png", fos);//方法一
			//方法二
			Image itemp=image.getScaledInstance(image.getWidth(),image.getHeight(),Image.SCALE_SMOOTH);
			PngEncoder pngEncoder = new PngEncoder(itemp);
			pngEncoder.setEncodeAlpha(true);
			pngEncoder.setCompressionLevel(5);
			fos.write(pngEncoder.pngEncode());
		}
		if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
			
			BufferedImage newBufferedImage = new BufferedImage(image.getWidth(),  
	                image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);  
	        newBufferedImage.getGraphics().drawImage(image, 0, 0, null);  
	  
	        Iterator<ImageWriter> iter = ImageIO  
	                .getImageWritersByFormatName("jpeg");  
	  
	        ImageWriter imageWriter = iter.next();  
	        ImageWriteParam iwp = imageWriter.getDefaultWriteParam();  
	  
	        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);  
	        iwp.setCompressionQuality(1.0F);  
	  
	        ImageOutputStream ios  =  ImageIO.createImageOutputStream(fos); 
	        imageWriter.setOutput(ios);  
	        IIOImage iio_image = new IIOImage(newBufferedImage, null, null);  
	        imageWriter.write(null, iio_image, iwp);  
	        imageWriter.dispose();
		}
		fos.flush();
		fos.close();
	}

	public Dimension getDimension(String fileName){
		File f = new File(fileName);
		try {
			return getDimension(f);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Dimension getDimension(File f) throws IOException{
		BufferedImage image = readImage(f.getAbsolutePath());
		if (image == null) {
			return new Dimension(0, 0);
		} else {
			return new Dimension(image.getWidth(), image.getHeight());
		}

	}

	/**
	 * 把图片印刷到图片上
	 * 
	 * @param targetImg
	 *            目标文件
	 * @param pressImg
	 *            水印文件
	 * @param position
	 *            水印位置 用1-9表示，0代表随机
	 */
	public void pressImage(String targetImg, String pressImg, int position) {
		try {
			// 目标文件
			File file = new File(targetImg);
			Image src = ImageIO.read(file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			if (wideth <= 300 && height <= 300) {
				return;
			}
			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);

			// 水印文件
			File file_press = new File(pressImg);
			if (!file_press.exists()) {
				System.out.println("水印图片不存在：" + pressImg);
				return;
			}
			Image src_press = ImageIO.read(file_press);
			int wideth_press = src_press.getWidth(null);
			int height_press = src_press.getHeight(null);
			// 根据position位置来计算x、y坐标，保证图片水印全在图片内，并且不紧靠图片的边缘
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
				position = NumberUtil.getRandomInt(9) + 1;
			}
			x = positions[(position - 1) / 3][(position - 1) % 3][0];
			y = positions[(position - 1) / 3][(position - 1) % 3][1];

			g.drawImage(src_press, x, y, wideth_press, height_press, null);
			// 水印文件结束
			g.dispose();
			
			ImageIO.write(image, "jpg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 把图片印刷到图片上，默认位置为图片右下角,图片分辨率小于300*300的不打水印
	 * 
	 * @param targetImg
	 *            目标文件
	 * @param pressImg
	 *            水印文件
	 */
	public void pressImage(String targetImg, String pressImg) {
		pressImage(targetImg, pressImg, 9);
	}

	public void pressText(String targetImg, String pressText, int color, int fontSize, int position) {
		pressText(targetImg, pressText, "宋体", Font.BOLD, color, fontSize, position);
	}

	/**
	 * 打印文字水印图片
	 * 
	 * @param targetImg
	 *            目标图片
	 * @param pressText
	 *            文字
	 * @param fontName
	 *            字体名
	 * @param fontStyle
	 *            字体样式
	 * @param color
	 *            字体颜色
	 * @param fontSize
	 *            字体大小
	 * @param position
	 *            水印位置 用1-9表示，0代表随机
	 */

	public static void pressText(String targetImg, String pressText, String fontName, int fontStyle, int color,
			int fontSize, int position) {
		try {
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			if (wideth <= 300 && height <= 300) {
				return;
			}
			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			g.setColor(new Color(color));
			g.setFont(new Font(fontName, fontStyle, fontSize));
			// 根据position位置来计算x、y坐标，保证文字水印全在图片内，并且不紧靠图片的边缘
			int x, y;
			if (position == 0) {
				x = NumberUtil.getRandomInt(wideth);
				x = x < fontSize * 2 ? fontSize * 2 : x;
				y = NumberUtil.getRandomInt(height);
				y = y < fontSize * 2 / 2 ? fontSize * 2 : y;
			} else {
				x = wideth * ((int) ((position - 1) % 3)) / 3 + fontSize * 2;
				y = height * (((int) ((position - 1) / 3))) / 3 + fontSize * 2;
			}
			if (x > wideth - fontSize * pressText.length() * 4 / 3) {
				x = wideth - fontSize * pressText.length() * 4 / 3;
			}
			if (y > height - fontSize) {
				y = height - fontSize;
			}

			g.drawString(pressText, x, y);
			g.dispose();
			ImageIO.write(image, "jpg", _file);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void transform(File src, File dest) {
		transform(src, dest, 1600);
	}

	public static void transform(File src, File dest, int nw) {
		try {
			AffineTransform transform = new AffineTransform();
			BufferedImage bis = ImageIO.read(src);
			int w = bis.getWidth();
			int h = bis.getHeight();
			int nh = (nw * h) / w;
			double sx = (double) nw / w;
			double sy = (double) nh / h;
			transform.setToScale(sx, sy);
			AffineTransformOp ato = new AffineTransformOp(transform, null);
			BufferedImage bid = new BufferedImage(nw, nh, BufferedImage.TYPE_3BYTE_BGR);
			ato.filter(bis, bid);
			ImageIO.write(bid, "jpg", dest);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 图像切割
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param descDir
	 *            切片目标文件夹
	 * @param destWidth
	 *            目标切片宽度
	 * @param destHeight
	 *            目标切片高度
	 * @param x x轴开始位置
	 * @param y y轴开始位置
	 */
	
	public static boolean cut(String srcPath, String dirPath, double toWidth, double toHeight, double x, double y) {
    	return cut(srcPath, dirPath, Math.round(toWidth), Math.round(toHeight), Math.round(x), Math.round(y));
    }
    public static boolean cut(String srcPath, String dirPath, float toWidth, float toHeight, float x, float y) {
    	return cut(srcPath, dirPath, Math.round(toWidth), Math.round(toHeight), Math.round(x), Math.round(y));
    }
    
	public static boolean cut(String srcImageFile, String descDir, int destWidth, int destHeight, int x, int y) {
		try {
			Image img;
			ImageFilter cropFilter;
			//String dir = null;
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getWidth(); // 源图宽度
			int srcHeight = bi.getHeight(); // 源图高度
			System.out.println("x轴:" + x);
			System.out.println("y轴:" + y);
			System.out.println("源图宽度:" + srcWidth);
			System.out.println("源图高度:" + srcHeight);
			System.out.println("切图宽度:" + destWidth);
			System.out.println("切图高度:" + destHeight);
			if (srcWidth >= destWidth && srcHeight >= destHeight) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight,
						Image.SCALE_DEFAULT);
				// 建立切片
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				cropFilter = new CropImageFilter(x , y,
						destWidth, destHeight);
				img = Toolkit.getDefaultToolkit().createImage(
						new FilteredImageSource(image.getSource(),
								cropFilter));
				BufferedImage tag = new BufferedImage(destWidth,
						destHeight, BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, null); // 绘制缩小后的图
				g.dispose();
				// 输出为文件
				File f = new File(descDir);
				ImageIO.write(tag, "JPEG", f);
				System.out.println("切片成功,切片位置" + descDir);
				// ImageUtils.pressText("水印",dir,"宋体",1,1,25,10,10);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Test
	public void test() throws IOException {
		scaleFixedImageFile("D:/a.jpg", "D:/b.jpg", 300, 300);
		
		//ImageJDKUtil.pressImage("D:/11111.jpg","D:/qqqq.png" , 0);
	}

	@Override
	public boolean cutImage(String srcPath, String dirPath, String toWidth,
			String toHeight, String imgX1, String imgY1, double rate) {
		boolean isCut=cut(srcPath, dirPath, Integer.valueOf(toWidth)*rate, Integer.valueOf(toHeight)*rate, Integer.valueOf(imgX1)*rate, Integer.valueOf(imgY1)*rate);
		return isCut;
	}
	
	@Override
	public boolean scaleRateImageFile2(String fromFileName, String toFileName,
			int toWidth, int toHeight) {
		try {
			this.scaleFixedImageFile(fromFileName, toFileName, toWidth, toHeight);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

}
