package com.leimingtech.core.util;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.math.RandomUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 视频工具类，调用ffmpeg进行视频处理
 * 
 * @author huanglei modify by wanghao 20140430
 * @date 2008-4-8
 */
public class VideoUtil {

	public static String taskid = "";
	public static String videoid = "";
	public static String srcPath = "";
	
	/**
	 * 截取视频随机6张图
	 * @param src
	 * @return
	 */
	public static void captureRandomImage(String src) {
		int duration = VideoUtil.getDuration(src);
		for (int i = 0; i < 6; i++) {
			captureImage(src, src + (i + 1) + ".jpg", RandomUtils.nextInt(duration) + 1);
		}
	}

	public static boolean captureDefaultImage(String src, String destImage) {
		int duration = 10;//
		if (duration < 30) {
			return captureImage(src, destImage, duration / 3+1);
		} else {
			return captureImage(src, destImage, 15);
		}
	}

	public static boolean captureImage(String src, String destImage, int startSecond) {
		int w = 720;
		int h = 480;
//		int[] WH =getWidthHeight(src);//获取视屏长宽
//		if(WH.length==2){
//			w = WH[0];
//			h = WH[1];
//		}
//		if(StringUtil.isNotEmpty(Config.getValue("VideoImageWidth"))&&StringUtil.isDigit(Config.getValue("VideoImageWidth"))){
//			w = Integer.parseInt(Config.getValue("VideoImageWidth"));
//		}
//		if(StringUtil.isNotEmpty(Config.getValue("VideoImageHeight"))&&StringUtil.isDigit(Config.getValue("VideoImageHeight"))){
//			h = Integer.parseInt(Config.getValue("VideoImageHeight"));
//		}
//		return captureImage2(src ,destImage);
		return captureImage(src, destImage, startSecond, w, h);
	}
	
	//方法一
	public static boolean captureImage(String src, String destImage, int ss, int width, int height) {
		String fileName = "ffmpeg";
		if (SystemUtils.OS_NAME.toLowerCase().indexOf("windows") >= 0) {
			fileName = ResourceUtil.getSysPath()+ "videotools\\" + "ffmpeg";
//			fileName = fileName.replaceAll("\\\\", "/");
//			System.out.println(fileName);
		}
		System.out.println(fileName+" -i "+src+" -y -f image2 -ss 15 -t 0.001 -s "+width+"*"+height+" "+destImage);
//		return exec(fileName + " -i " + src + " -y -f image2 -ss " + ss + " -t 0.001 -s " + width + "x" + height + " "
//				+ destImage, null, ResourceUtil.getSysPath()+"/" + "videotools/");
		return exec2(fileName+" -i "+src+" -y -f image2 -ss " + ss + " -t 0.001 -s "+ width + "x" + height +" "+destImage);
	}

	/**
	 * 方法二
	 * @param INPUT_PATH 视频路径
	 * @param IMG_PATH 图片的地址
	 * @return foolean
	 */
	public static boolean captureImage2(String INPUT_PATH,String IMG_PATH ) {
		int duration = VideoUtil.getDuration(INPUT_PATH);//
		int s = 15;//默认第10秒
		if (duration < 30) {
			s = duration / 3 ;
		}
        System.out.println("IMG_PATH="+IMG_PATH);
        if(new File(IMG_PATH).exists()) return true;
        List<String> commend = new ArrayList<String>();
        commend.add(ResourceUtil.getSysPath()+ "videotools\\" + "ffmpeg");
        commend.add("-i");
        commend.add(INPUT_PATH);
        commend.add("-y");
        commend.add("-f");
        commend.add("image2");
        commend.add("-ss");
        commend.add(s+"");
        commend.add("-t");
        commend.add("0.001");
        commend.add("-s");
        commend.add("200x150");
        commend.add(IMG_PATH);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            builder.start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	 
	public final static String _ConvertAvi2Flv = " -of lavf -oac mp3lame -lameopts abr:br=56 -ovc lavc -lavcopts vcodec=flv:vbitrate=200:mbd=2:mv0:trell:v4mv:cbp:last_pred=3:dia=4:cmp=6:vb_strategy=1 -vf scale=512:-3 -ofps 12	 -srate 22050";
	
	public final static String _ConvertRm2Flv = " -of lavf -oac mp3lame -lameopts abr:br=56 -ovc lavc -lavcopts vcodec=flv:vbitrate=200:mbd=2:mv0:trell:v4mv:cbp:last_pred=3 -srate 22050";
	public final static String _ConvertRm2Mp4 = " -vf dsize=480:360:2,scale=-8:-8,harddup -oac faac -faacopts mpeg=4:object=2:raw:br=128 -of lavf -lavfopts format=mp4 -ovc x264 -sws 9 -x264encopts nocabac:level_idc=30:bframes=0:bitrate=512:threads=auto:turbo=1:global_header:threads=auto:subq=5:frameref=6:partitions=all:trellis=1:chroma_me:me=umh";
	public static boolean convert2Mp4(String src, String dest) {
		String fileName = "mencoder ";
		System.out.println(SystemUtils.OS_NAME);
		if (SystemUtils.OS_NAME.toLowerCase().indexOf("windows") >= 0) {
			fileName = "\"" + ResourceUtil.getSysPath() + "/videotools/" + "mencoder.exe\" ";
			src = "\"" + src + "\"";
			dest = "\"" + dest + "\"";
		}
		if (src.toLowerCase().lastIndexOf(".mp4") != -1) {
			return true;
		} else if (src.toLowerCase().lastIndexOf(".rm") != -1 || src.toLowerCase().lastIndexOf(".rmvb") != -1) {
			return false;
			/*return exec(fileName + src + " -o " + dest + " " + _ConvertRm2Mp4, null, ResourceUtil.getSysPath()
					+ "/videotools/");*/
		} else {
			return exec(fileName + src + " -o " + dest + " " + _ConvertRm2Mp4, null, ResourceUtil.getSysPath()
					+ "/videotools/");
		}
	}
	
	public static boolean convert2FlvSlit(String src, String dest, int ss, int endpos) {
		String fileName = "mencoder ";
		if (SystemUtils.OS_NAME.toLowerCase().indexOf("windows") >= 0) {
			fileName = "\"" + ResourceUtil.getSysPath()+"/" + "videotools/" + "mencoder.exe\" ";
			src = "\"" + src + "\"";
			dest = "\"" + dest + "\"";
		}
		if (src.toLowerCase().lastIndexOf(".rm") != -1 || src.toLowerCase().lastIndexOf(".rmvb") != -1) {
			return exec(fileName + src + " -o " + dest + " " + _ConvertRm2Flv, null, ResourceUtil.getSysPath()+"/"
					+ "videotools/");
		} else {
			return exec(fileName + src + " -o " + dest + " -ss " + ss + " -endpos " + endpos + " " + _ConvertAvi2Flv,
					null, ResourceUtil.getSysPath()+"/" + "videotools/");
		}
	}

	public final static String _Identify = " -nosound -vc dummy -vo null";
	
	/**
	 * 获得视频时长
	 * @param src
	 * @return
	 */
	public static int getDuration(String src) {
		String fileName = "mplayer ";
		if (SystemUtils.OS_NAME.toLowerCase().indexOf("windows") >= 0) {
			fileName = "\"" + ResourceUtil.getSysPath()+"/" + "videotools/" + "mplayer.exe\" ";
			src = "\"" + src + "\"";
		}
		String command = fileName + " -identify " + src + " " + _Identify;
		int duration = 0;
		try {
			Process process = Runtime.getRuntime()
					.exec(command, null, new File(ResourceUtil.getSysPath()+"/" + "videotools/")); // 调用外部程序

			InputStream is = process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			try {
				while ((line = br.readLine()) != null) {
					System.out.println(line);
					if (line.indexOf("ID_LENGTH=") > -1) {
						duration = (int) Math.ceil(Double.parseDouble(line.substring(10)));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				return duration;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return duration;
		}
		return duration;
	}

	public static int[] getWidthHeight(String src) {
		String fileName = "mplayer ";
		if (SystemUtils.OS_NAME.toLowerCase().toLowerCase().indexOf("windows") >= 0) {
			fileName = "\"" + ResourceUtil.getSysPath()+"/" + "videotools/" + "mplayer.exe\" ";
			src = "\"" + src + "\"";
		}
		String command = fileName + " -identify " + src + " " + _Identify;
		int[] WidthHeight = new int[] { 0, 0 };
		try {
			Process process = Runtime.getRuntime()
					.exec(command, null, new File(ResourceUtil.getSysPath()+"/" + "videotools/")); // 调用外部程序

			InputStream is = process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			try {
				while ((line = br.readLine()) != null) {
					System.out.println(line);
					if (line.indexOf("ID_VIDEO_WIDTH=") > -1) {
						WidthHeight[0] = (int) Math.ceil(Double.parseDouble(line.substring(15)));
					}
					if (line.indexOf("ID_VIDEO_HEIGHT=") > -1) {
						WidthHeight[1] = (int) Math.ceil(Double.parseDouble(line.substring(16)));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				return WidthHeight;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return WidthHeight;
		}
		return WidthHeight;
	}

	public static boolean exec(String command, String[] args, String dir) {
		try {
			Process process = Runtime.getRuntime().exec(command, args, new File(dir)); // 调用外部程序

			final InputStream is1 = process.getInputStream();
			new Thread(new Runnable() {
				public void run() {
					BufferedReader br = new BufferedReader(new InputStreamReader(is1));
					String line = null;
					try {
						while ((line = br.readLine()) != null) {
							System.out.println(line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start(); // 启动单独的线程来清空process.getInputStream()的缓冲区
			InputStream is2 = process.getErrorStream();
			BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
			StringBuffer buf = new StringBuffer(); // 保存输出结果流
			String line = null;
			while ((line = br2.readLine()) != null)
				buf.append(line); // 循环等待ffmpeg进程结束
			System.out.println("exec输出为：" + buf);
//			is1.close();
//			is2.close();
//			br2.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public static boolean exec2(String command) {
		try {
			System.out.println("command:" + command);
			Process process = Runtime.getRuntime().exec(command); // 调用外部程序

			final InputStream is1 = process.getInputStream();
			new Thread(new Runnable() {
				public void run() {
					BufferedReader br = new BufferedReader(new InputStreamReader(is1));
					String line = null;
					try {
						while ((line = br.readLine()) != null){
							System.out.println(line);
						}
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					};
				}
			}).start(); // 启动单独的线程来清空process.getInputStream()的缓冲区
			InputStream is2 = process.getErrorStream();
			BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
			StringBuffer buf = new StringBuffer(); // 保存输出结果流
			String line = null;
			while ((line = br2.readLine()) != null)
				buf.append(line); // 循环等待ffmpeg进程结束
			System.out.println("输出为：" + buf);
//			is1.close();
//			is2.close();
//			br2.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/*public static void setTask(String taskID,String fileID,String fileType,String filePath,String Status){
		if(uptaskMap.get(taskID)!=null){
			String[][] tempArr = (String[][]) uptaskMap.get(taskID);
			boolean hasFile = false;
			for (int i = 0; i < tempArr.length; i++) {
				if(tempArr[i][0].equals(fileID)){
					hasFile = true;
					tempArr[i] = new String[]{fileID,fileType,filePath,Status};
					uptaskMap.put(taskID, tempArr);
					return;
				}
			}
			if(hasFile==false){
				String[][] newArr = new String[tempArr.length+1][1];
				for (int i = 0; i < tempArr.length; i++) {
					newArr[i] = tempArr[i];
				}
				newArr[tempArr.length] = new String[]{fileID,fileType,filePath,Status};
				uptaskMap.put(taskID, newArr);
			}
		}else{
			String[][] fileArr = {{fileID,fileType,filePath,Status}};
			uptaskMap.put(taskID, fileArr);
		}
	}*/
	
	public static void main(String[] args) {
		// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
		//方法一
//		VideoUtil.captureDefaultImage("E:\\metroic\\lmcms-framework\\WebRoot\\upload\\videofile\\test.mp4", "E:\\metroic\\lmcms-framework\\WebRoot\\upload\\videofile\\1wwww11.jpg");// ok
		//方法二
//		VideoUtil.captureImage2("E:\\metroic\\lmcms-framework\\WebRoot\\upload\\videofile\\IMGP0084.AVI" ,"E:\\metroic\\lmcms-framework\\WebRoot\\upload\\videofile\\2222.jpg");
//		System.out.println(ResourceUtil.getSysPath());
//		VideoUtil.convert2Mp4("D:\\project\\java\\lmcms\\lmcms-framework\\WebRoot\\wwwroot\\www\\upload\\video\\20140721\\1405911735684022354.avi", "D:\\project\\java\\lmcms\\lmcms-framework\\WebRoot\\wwwroot\\www\\upload\\video\\20140721\\1405911735684022354.mp4");
//		int[] str = VideoUtil.getWidthHeight("E:\\metroic\\lmcms-framework\\WebRoot\\wwwroot\\1\\vodeofile\\20140524\\1400910151366.mp4");
//		for(int i=0 ;i<str.length;i++){
//			System.out.println(str[i]);
//		}
		VideoUtil.captureDefaultImage("d:/qqqq.flv", "d:/qqqq.jpg");
	}
}
