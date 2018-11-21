package com.wenqiang.reptile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;











import com.csvreader.CsvWriter;
import com.wenqiang.model.Car;

/** 
 * @author 作者  文强
 * @version 创建时间：2018年3月29日 上午9:10:44 
 * 类说明 
 */
public class ClimbToData  extends  Thread{

	/**
	 * img  标签
	 * **/	
	 private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
	 /**
	  * src 的路径
	  * 
	  * **/
	 private static final String IMGSRC_REG = "[a-zA-z]+://[^\\s]*";	
	
	
	 /**
	  * 图片存放地址
	  * 
	  * **/
	 private static final String IMGURL = "d:\\image\\";
	 
	 /**
	  * 图片相对地址
	  * 
	  * **/
	 private static final String IMGURLPIC = "image\\";
	 
	 
	 
	
	
	/**
	 * html脚本获取
	 * **/
	public static String getHtmlResourceRUL(String url,String encoding){
		StringBuffer buffer = new StringBuffer();
		InputStreamReader isr=null;
		URL urlobj=null;
		URLConnection uc=null;
		BufferedReader br=null;
		try {
			urlobj = new URL(url);
			 uc = urlobj.openConnection();
			isr = new InputStreamReader(uc.getInputStream());
			br=new BufferedReader(isr);
			String temp =null;
			while ((temp=br.readLine())!= null) {
			     
				buffer.append(temp);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(isr!=null){
					isr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buffer.toString();
	}
	

	public static void main(String[] args) throws Exception {
		String url="http://car.bitauto.com/brandlist.html";
		String uri="http://car.bitauto.com/";
		String encoding="utf-8";
		String html = getHtmlResourceRUL(url, encoding);
		Document parse = Jsoup.parse(html);
		String html1=parse.select("[class=bybrand_list]").html();
		String[] info = html1.split("<dd class=\"line\"></dd>");
	
		List<Car> list=new ArrayList<Car>();
 		 for(int i=0 ;i<info.length;i++){
 			String uuid=new Random().nextInt(888888888)+"";
			 Document brandname = Jsoup.parse(info[i]);  
			   Elements elements=brandname.select("[class=brandname]");
			   Elements elements1=brandname.select("a");
			   String htm = getHtmlResourceRUL(uri+elements1.attr("href"), encoding);
			   String today = elements.get(0).text();
			   Car car=new Car();
			   car.setCarId(uuid);
			   car.setCarlev(1+"");
			   car.setParentNode(0+"");
			   car.setCarBrandName(today);
			  
			   Document parseimg = Jsoup.parse(htm);
			  String  imghtml= parseimg.select("[class=brand-logo]").html();
			  List<String> strimg=  getImageSrc(getImageUrl(imghtml));
			 if(org.apache.commons.lang3.StringUtils.isNotEmpty(strimg.get(0))){
				 download(strimg.get(0),uuid+".png",IMGURL);
				  car.setCarBrandPic(IMGURLPIC+uuid+".png");
			 }
			  
			
			  list.add(car);
			  String[] infoblank = brandname.select("[class=have]").html().split("</h2>");
			  for(int j=0;j<infoblank.length;j++){
				  Document branblank = Jsoup.parse(infoblank[j]);  
				  Elements elementsB= branblank.select("a");
				   Car car1=new Car();
				   String uid=new Random().nextInt(888888888)+"";
				   car1.setCarId(uid);
				   car1.setCarlev(2+"");
				   car1.setParentNode(uuid);
				   car1.setCarBrandName(elementsB.get(0).text());
				   list.add(car1);
				   String[] infoul = brandname.select("[class=have]").html().split("</ul>");
				   for(int y=0;y<infoul.length;y++){
					   String[]  infoli= infoul[y].split("<li>");
					       for(int u=1;u<infoli.length;u++){
					    	  
					    	   String[]  infoa=  infoli[u].split("</a>");
					    	    	  Car car2=new Car();
									   car2.setCarId(new Random().nextInt(888888888)+"");
									   car2.setCarlev(3+"");
									   car2.setParentNode(uid);
									   car2.setCarBrandName(Jsoup.parse(infoa[0]).select("a").get(0).text());
									   
									   car2.setCarBrandType(Jsoup.parse(infoa[1]).select("a").get(0).text());
									   
									   list.add(car2);
									 
					       } 
					   
				   }
				
			  }
			
		 }
	     
 		wintCsv(list);
		System.out.println(list);
		
		
	}
	
	

	
	/**
	 * 获得html里img标签
	 * */
	  private static List<String> getImageUrl(String html){
	        Matcher matcher=Pattern.compile(IMGURL_REG).matcher(html);
	        List<String>listimgurl=new ArrayList<String>();
	        while (matcher.find()){
	            listimgurl.add(matcher.group());
	        }
	        return listimgurl;
	    }
	
	  /**
	   * 获得src里的路径
	   * **/
	  private static List<String> getImageSrc(List<String> listimageurl){
	        List<String> listImageSrc=new ArrayList<String>();
	        for (String image:listimageurl){
	            Matcher matcher=Pattern.compile(IMGSRC_REG).matcher(image);
	            while (matcher.find()){
	                listImageSrc.add(matcher.group().substring(0, matcher.group().length()-1));
	            }
	        }
	        return listImageSrc;
	    }
	  
	  /**
	   * csv
	   * 写入
	   * **/
	  public static void wintCsv(List<Car> cat) throws IOException{
		  // 定义一个CSV路径  
		    String csvFilePath = "D://Car.csv";  
		    try {  
		        // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);  
		        CsvWriter csvWriter = new CsvWriter(csvFilePath, ',', Charset.forName("gbk"));  
		        // 写表头  
		        String[] csvHeaders = { "编号", "父类id", "品牌","类型","图片","级别"};  
		        csvWriter.writeRecord(csvHeaders);  
		        // 写内容  
		        for (int i = 0; i < cat.size(); i++) {  
		        	
		            String[] csvContent = { cat.get(i).getCarId().toString(),cat.get(i).getParentNode(), cat.get(i).getCarBrandName(),cat.get(i).getCarBrandType(),cat.get(i).getCarBrandPic(),cat.get(i).getCarlev() };  
		            csvWriter.writeRecord(csvContent);  
		        }  
		        csvWriter.close();  
		        System.out.println("--------CSV文件已经写入--------");  
		    } catch (IOException e) {  
		        e.printStackTrace();  
		    }  
	  }
	  
	  /**
	   * 下载图片到本地
	   * **/
	  
	    public static void download(String urlString, String filename,String savePath) throws Exception {    
	        // 构造URL    
	        URL url = new URL(urlString);    
	        // 打开连接    
	        URLConnection con = url.openConnection();    
	        //设置请求超时为5s    
	        con.setConnectTimeout(5*1000);    
	        // 输入流    
	        InputStream is = con.getInputStream();    
	        
	        // 1K的数据缓冲    
	        byte[] bs = new byte[1024];    
	        // 读取到的数据长度    
	        int len;    
	        // 输出的文件流    
	       File sf=new File(savePath);    
	       if(!sf.exists()){    
	           sf.mkdirs();    
	       }    
	       OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);    
	        // 开始读取    
	        while ((len = is.read(bs)) != -1) {    
	          os.write(bs, 0, len);    
	        }    
	        // 完毕，关闭所有链接    
	        os.close();    
	        is.close();    
	    }      
	  
}
