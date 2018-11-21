package com.leimingtech.core.util.image;


import com.leimingtech.core.util.ResourceUtil;
import org.junit.Test;


/**
 * 图片处理器工厂<br>
 * 
 * @author wanghao
 * 2014-5-13
 */
public abstract class ThumbnailCreatorFactory {
	private ThumbnailCreatorFactory(){}
//	public static String CREATORTYPE="javaimageio";
	
	/**
	 * 返回图片处理器
	 * @return 
	 * 当{@link #creatortype} 为javaimageio时使用{@link ImageJDKCreator }生成器<br>
	 * 当{@link #creatortype} 为imagemagick时使用{@link ImageMagickCreator }生成器<br>
	 */
	public static final IThumbnailCreator getCreator(){
		String creatortype = ResourceUtil.getImageCreatortype();
		IThumbnailCreator creator = null;
		try {
			creator = (IThumbnailCreator)Class.forName("com.leimingtech.core.util.image."+creatortype).newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return creator;
		
	}
	
	@Test
	public void test() {
		IThumbnailCreator thumbnailCreator = ThumbnailCreatorFactory.getCreator();
		thumbnailCreator.scaleFixedImageFile("D:/1.jpg", "D:/2.jpg", 200, 200);
	}
}
