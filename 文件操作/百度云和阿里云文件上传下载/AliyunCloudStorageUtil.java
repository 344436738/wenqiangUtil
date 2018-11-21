package com.leimingtech.common.utils.cloudStorage;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.ServiceException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSErrorCode;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.*;
import org.junit.Test;

import java.io.*;
import java.util.List;

/**
 * 阿里云云存储
 * @author lkang
 * @time 2014-05-16
 */
public class AliyunCloudStorageUtil {
	//FIXME 阿里云云存储帐号放入配置文件
	public static final String ACCESS_ID = "NRsDH3SZjGRVzL71"; //accessid 阿里云提供
    public static final String ACCESS_KEY = "LXBVsOfaQZ3hdccI385BiJ7j1zmvWo"; //accesskey 阿里云提供
    
    
    @Test
    public void test() {
    	// 全局唯一的，保证bucketName不与别人重复
    	String bucketName = ACCESS_ID + "-object";
    	// 上传的文件的名称
    	String key = "logo.gif";
    	// 使用默认的OSS服务器地址初始化创建OSSClient对象
        OSSClient client = new OSSClient(ACCESS_ID, ACCESS_KEY);
        // 需要上传的文件
        String uploadFilePath = "F:\\images\\xitong-001.jpg";
        // 需要下载的文件
        String downloadFilePath = "http://www.leimingtech.com/images/logo.gif";
        // 下载时需要输出的文件
        String outfilename = "";
        // 创建Bucket
        ensureBucket(client, bucketName);
        try {
        	// 把Bucket设置为所有人可读，可不进行设置
            setBucketPublicReadable(client, bucketName);
            // 列出用户所有的Bucket
            bucketList(client);
            // 删除Bucket
            deleteBucket(client, bucketName);
            // 开始上传
            uploadFile(client, bucketName, key, uploadFilePath);
            // 开始下载
            downloadFile(client, bucketName, key, downloadFilePath, outfilename);
            // 列出Bucket中的Object
            listObjects(client, bucketName);
        } catch (OSSException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			// 删除一个Bucket和其中的Objects
            deleteBucket(client, bucketName);
        }
	}
    
    /**
     * 创建Bucket,判断是否存在，不存创建
     * @param client
     * @param bucketName 全局唯一的，保证bucketName不与别人重复
     * @throws OSSException
     * @throws ClientException
     */
    @SuppressWarnings("deprecation")
	public static void ensureBucket(OSSClient client, String bucketName)
            throws OSSException, ClientException{
        try {
        	// 获取Bucket的存在信息
        	boolean exists = client.doesBucketExist(bucketName);
        	// 输出结果
        	if (exists) {
        	    System.out.println("Bucket exists");
        	} else {
        	    System.out.println("Bucket not exists");
        	    // 创建bucket
        	    client.createBucket(bucketName);
        	}
        } catch (ServiceException e) {
            if (!OSSErrorCode.BUCKES_ALREADY_EXISTS.equals(e.getErrorCode())) {
                // 如果Bucket已经存在，则忽略
                throw e;
            }
        }
    }
    
    /**
     * 把Bucket设置为所有人可读 
     * @param client
     * @param bucketName 全局唯一的，保证bucketName不与别人重复
     * @throws OSSException
     * @throws ClientException
     */
    public static void setBucketPublicReadable(OSSClient client, String bucketName)
            throws OSSException, ClientException {
        //创建bucket
        client.createBucket(bucketName);

        //设置bucket的访问权限，public-read-write权限
        client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
    }
    
    /**
     * 列出用户所有的Bucket
     * @param client
     */
    public static void bucketList(OSSClient client){
    	List<Bucket> buckets = client.listBuckets();
    	// 遍历Bucket
    	for (Bucket bucket : buckets) {
    	    System.out.println(bucket.getName());
    	}
    }
    
    /**
     * 上传文件
     * @param client
     * @param bucketName 全局唯一的，保证bucketName不与别人重复
     * @param key 上传的文件的名称
     * @param filename
     * @throws OSSException
     * @throws ClientException
     * @throws FileNotFoundException
     */
    public static void uploadFile(OSSClient client, String bucketName, String key, String filename)
            throws OSSException, ClientException, FileNotFoundException {
    	// 获取指定文件的输入流
        File file = new File(filename);

        // 创建上传Object的Metadata
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length());
        // 可以在metadata中标记文件类型,也可以不进行设置
        objectMeta.setContentType("image/jpeg");

        InputStream input = new FileInputStream(file);
        client.putObject(bucketName, key, input, objectMeta);
    }
    
    /**
     * 下载文件
     * @param client
     * @param bucketName 全局唯一的，保证bucketName不与别人重复
     * @param key 下载的文件的名称
     * @param filename 暂时没用
     * @param outfilename
     * @throws OSSException
     * @throws ClientException
     */
    public static void downloadFile(OSSClient client, String bucketName, String key, String filename, String outfilename)
            throws OSSException, ClientException {
        /*client.getObject(new GetObjectRequest(bucketName, key),
                new File(filename));*/
        // 新建GetObjectRequest
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        // 下载Object到文件
        client.getObject(getObjectRequest, new File(outfilename));
        //ObjectMetadata objectMetadata = client.getObject(getObjectRequest, new File(outfilename));
        //objectMetadata = client.getObjectMetadata(bucketName, key);
    }
    
    /**
     * 删除一个Bucket和其中的Objects 
     * @param client
     * @param bucketName 全局唯一的，保证bucketName不与别人重复
     * @throws OSSException
     * @throws ClientException
     */
    public static void deleteBucket(OSSClient client, String bucketName)
            throws OSSException, ClientException {

        ObjectListing ObjectListing = client.listObjects(bucketName);
        List<OSSObjectSummary> listDeletes = ObjectListing
                .getObjectSummaries();
        for (int i = 0; i < listDeletes.size(); i++) {
            String objectName = listDeletes.get(i).getKey();
            // 如果不为空，先删除bucket下的文件
            client.deleteObject(bucketName, objectName);
        }
        client.deleteBucket(bucketName);
    }
    
    /**
     * 列出Bucket中的Object
     * @param client
     * @param bucketName
     */
    public static void listObjects(OSSClient client, String bucketName){
        // 获取指定bucket下的所有Object信息
        ObjectListing listing = client.listObjects(bucketName);
        // 遍历所有Object
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            System.out.println(objectSummary.getKey());
        }
    }
    
    /**
     * 读取Object并下载
     * @param client
     * @param bucketName
     * @param key
     * @param outfilepath
     */
    public static void getObject(OSSClient client, String bucketName, String key, String outfilepath) {
        try {
        	// 获取Object，返回结果为OSSObject对象
            OSSObject object = client.getObject(bucketName, key);
            // 获取ObjectMeta
            //ObjectMetadata meta = object.getObjectMetadata();
            // 获取Object的输入流
            InputStream objectContent = object.getObjectContent();
            // 处理Object
            FileOutputStream out = new FileOutputStream(outfilepath);
			int temp = 0;
			byte[] buf = new byte[1024];
			while((temp = objectContent.read(buf))!=-1){   
				out.write(buf, 0, temp);   
            }
        	out.close();
            // 关闭流
			objectContent.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
