package com.leimingtech.common.utils.cloudStorage;

import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.auth.BCSSignCondition;
import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.*;
import com.baidu.inf.iis.bcs.request.*;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.io.*;
import java.util.List;


/**
 * 百度云存储
 * @author lkang
 * @time 2014-05-16
 */
public class BaiduCloudStorageUtil {
	
	public static final Log log = LogFactory.getLog(BaiduCloudStorageUtil.class);
	public static final String host = "bcs.duapp.com";
    public static final String SECRET_KEY = "ZQQjhSeaAyVUr2MnlaopIDNYBmv9nrSQ"; //SECRET_KEY 百度云提供,百度云简称SK
    public static final String API_KEY = "dILNVyyujciwU6GeNEQG5zgA"; //API_KEY 百度云,百度云简称AK
	
    @Test
	public void test() {
		// 初始化BCSCredentials
		BCSCredentials credentials = new BCSCredentials(API_KEY, SECRET_KEY);
		// 初始化BaiduBCS
		BaiduBCS baiduBCS = new BaiduBCS(credentials, host);
		// 设置编码
		baiduBCS.setDefaultEncoding("UTF-8"); // Default UTF-8
		// 要上传的文件
		String filepath = "F:\\images\\xitong-001.jpg";
		// 目标文件夹
		String bucket = "bucket-leimingtech";
		try {
			// 文件夹列表
			listBucket(baiduBCS);
			// 创建 文件夹
			createBucket(baiduBCS, bucket);
			// 删除文件夹
			deleteBucket(baiduBCS, bucket);
			// 上传文件
			putObjectByInputStream(baiduBCS, bucket, filepath);
			// 下载文件
			getObjectWithDestFile(baiduBCS, bucket, "/images", "F:\\aaa.jpg");
			// 删除文件
			deleteObject(baiduBCS, bucket, "/images");
			// 某个文件夹的文件列表
			listObject(baiduBCS, bucket);
		} catch (BCSServiceException e) {
			log.warn("Bcs return:" + e.getBcsErrorCode() + ", "
					+ e.getBcsErrorMessage() + ", RequestId="
					+ e.getRequestId());
		} catch (BCSClientException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建 Bucket，也可以在百度云创建，最多20个
	 * @param baiduBCS
	 */
	public static void createBucket(BaiduBCS baiduBCS, String bucket) {
		//参数说明：1、文件夹名称 2、权限：public, PublicRead, PublicWrite, PublicReadWrite, PublicControl;
		CreateBucketRequest createbucket = new CreateBucketRequest(bucket, X_BS_ACL.PublicReadWrite);
		baiduBCS.createBucket(createbucket);
	}
	
	/**
	 * Bucket列表，文件夹列表
	 * @param baiduBCS
	 */
	public static void listBucket(BaiduBCS baiduBCS) {
		ListBucketRequest listBucketRequest = new ListBucketRequest();
		BaiduBCSResponse<List<BucketSummary>> response = baiduBCS.listBucket(listBucketRequest);
		for (BucketSummary bucket : response.getResult()) {
			log.info(bucket);
		}
	}
	
	/**
	 * 删除文件夹
	 * @param baiduBCS
	 * @param bucket
	 */
	public static void deleteBucket(BaiduBCS baiduBCS, String bucket) {
		baiduBCS.deleteBucket(bucket);
	}
	
	/**
	 * 上传
	 * @param baiduBCS
	 * @param filepath 上传的文件
	 * @throws FileNotFoundException
	 */
	public static void putObjectByInputStream(BaiduBCS baiduBCS, String bucket, String filepath) throws FileNotFoundException {
		// 获取指定文件的输入流
		File file = new File(filepath);
		InputStream fileContent = new FileInputStream(file);
		// 创建上传Object的Metadata
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType("image/jpg");
		objectMetadata.setContentLength(file.length());
		// 上传，参数说明：1、bucketName即文件夹名称 2、上传的文件名称 3、输入流 4、对象描述
		PutObjectRequest request = new PutObjectRequest(bucket, "/" + file.getName(), fileContent, objectMetadata);
		//返回结果
		ObjectMetadata result = baiduBCS.putObject(request).getResult();
		log.info(result);
	}
	
	/**
	 * 获取上传后的路径
	 * @param baiduBCS
	 * @param filepath 必须包括"/"，如"/images"
	 */
	public static void generateUrl(BaiduBCS baiduBCS, String bucket, String filepath) {
		GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest(HttpMethodName.GET, bucket, filepath);
		generateUrlRequest.setBcsSignCondition(new BCSSignCondition());
		generateUrlRequest.getBcsSignCondition().setIp("1.1.1.1");
		generateUrlRequest.getBcsSignCondition().setTime(123455L);
		generateUrlRequest.getBcsSignCondition().setSize(123455L);
		System.out.println(baiduBCS.generateUrl(generateUrlRequest));
	}
	
	/**
	 * 下载文件
	 * @param baiduBCS
	 * @param filepath 文件名
	 */
	public static void getObjectWithDestFile(BaiduBCS baiduBCS, String bucket, String filepath, String outfilepath) {
		try {
			GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, filepath);
			BaiduBCSResponse<DownloadObject> downloadobj = baiduBCS.getObject(getObjectRequest);
			DownloadObject obj = downloadobj.getResult();
			InputStream in = obj.getContent();
			FileOutputStream out = new FileOutputStream(outfilepath);
			int temp = 0;
			byte[] buf = new byte[1024];
			while((temp = in.read(buf))!=-1){   
				out.write(buf, 0, temp);   
            }
			System.out.println(obj.getContent());
			in.close();
        	out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 删除文件
	 * @param baiduBCS
	 * @param filepath 文件名
	 */
	public static void deleteObject(BaiduBCS baiduBCS, String bucket, String filepath) {
		Empty result = baiduBCS.deleteObject(bucket, filepath).getResult();
		log.info(result);
	}
	
	/**
	 * 某个文件夹的文件列表
	 * @param baiduBCS
	 */
	public static void listObject(BaiduBCS baiduBCS, String bucket) {
		ListObjectRequest listObjectRequest = new ListObjectRequest(bucket);
		listObjectRequest.setStart(0);
		listObjectRequest.setLimit(20);
		BaiduBCSResponse<ObjectListing> response = baiduBCS.listObject(listObjectRequest);
		log.info("we get [" + response.getResult().getObjectSummaries().size() + "] object record.");
		for (ObjectSummary os : response.getResult().getObjectSummaries()) {
			log.info(os.toString());
		}
	}

}
