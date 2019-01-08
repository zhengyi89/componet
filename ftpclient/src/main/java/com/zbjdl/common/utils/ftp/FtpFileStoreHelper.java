package com.zbjdl.common.utils.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class FtpFileStoreHelper {
	private String ip;
	private Integer port;
	private String userName;
	private String password;
	private String imageServer;
	private String name;
	private boolean imageCompress = true;
	public static int DEFAULT_IMAGE_WIDTH = 910;
	public static int DEFAULT_IMAGE_HEIGHT = 1500;

	private FtpFileStoreHelper(){
	}
	
	public static FtpFileStoreHelper getHelper(String name){
		FtpFileStoreHelper helper = new FtpFileStoreHelper();
		helper.name = name;
		
		Properties properties = new Properties();
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("ftpfilestore_"+name+".properties");
			properties.load(is);
		} catch (Exception e) {
			throw new RuntimeException("read properties fail : "+e.getMessage(), e);
		}
		helper.userName = properties.getProperty("username");
		if(helper.userName==null || helper.userName.trim().length()==0){
			throw new RuntimeException("username must be specified");
		}
		helper.imageServer = properties.getProperty("imageserver");
		if(helper.imageServer==null || helper.imageServer.trim().length()==0){
			throw new RuntimeException("imageserver must be specified");
		}
		helper.password = properties.getProperty("password");
		if(helper.password==null || helper.password.trim().length()==0){
			throw new RuntimeException("password must be specified");
		}
		helper.ip = properties.getProperty("ip");
		if(helper.ip==null || helper.ip.trim().length()==0){
			throw new RuntimeException("ip must be specified");
		}
		String pt = properties.getProperty("port");
		if(pt!=null){
			helper.port = new Integer(pt);
		}
		return helper;
	}
	
	/**
	 * 上传文件
	 * @param file 文件对象
	 * @param fileExtension 文件扩展名
	 * @return
	 */
	public String upload(File file,String fileExtension){
		InputStream is = compressPic(file, fileExtension);
		return imageServer+"/"+name+"/"+FtpFileStoreUtils.upload(is, fileExtension, ip, port, userName, password);
	}
	
	/**
	 * 上传文件
	 * @param is 文件流
	 * @param fileExtension 文件扩展名
	 * @return
	 */
	public String upload(InputStream is,String fileExtension){
		is = compressPic(is, fileExtension);
		return imageServer+"/"+name+"/"+FtpFileStoreUtils.upload(is, fileExtension, ip, port, userName, password);
	}
	/**
	 * 上传没有压缩的文件
	 * @param file 文件对象
	 * @param fileExtension 文件扩展名
	 * @return
	 */
	public String uploadFile(File file,String fileExtension){
		return imageServer+"/"+name+"/"+FtpFileStoreUtils.upload(file, fileExtension, ip, port, userName, password);
	}
	public boolean uploadAndOverwrite(InputStream is,String fileUrl){
		String str = imageServer+"/"+name+"/";
		if(fileUrl.startsWith(str)){
			fileUrl = fileUrl.substring(str.length());
		}
		is = compressPic(is, fileUrl);
		return FtpFileStoreUtils.uploadAndOverwrite(is, fileUrl, ip, port, userName, password);
	}
	
	public boolean uploadAndOverwrite(File file,String fileUrl){
		String str = imageServer+"/"+name+"/";
		if(fileUrl.startsWith(str)){
			fileUrl = fileUrl.substring(str.length());
		}
		InputStream is = compressPic(file, fileUrl);
		return FtpFileStoreUtils.uploadAndOverwrite(is, fileUrl, ip, port, userName, password);
	}
	
	/**
	 * 删除文件
	 * @param filePath 文件路径
	 */
	public void delete(String filePath){
		FtpFileStoreUtils.delete(filePath, ip, port, userName, password);
	}
	
	/**
	 * 获取文件
	 * @param filePath 文件路径
	 * @return
	 */
	public InputStream getStream(String filePath){
		return FtpFileStoreUtils.getStream(filePath, ip, port, userName, password);
	}
	
	public static InputStream compressPic(File file, String fileName){
		FileInputStream fi = null;;
		try {
			fi = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw FtpException.FILE_EXCEPTION.newInstance(e);
		}
		
		if(fileName!=null && (fileName.endsWith("jpg") || fileName.endsWith("gif") || fileName.endsWith("png") || fileName.endsWith("bmp"))){
			return ImageCompressUtils.compressImage(fi, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
		}
		else{
			return fi;
		}
	}
	
	public static InputStream compressPic(InputStream is, String fileName){
		if(fileName!=null && (fileName.endsWith("jpg") || fileName.endsWith("gif") || fileName.endsWith("png") || fileName.endsWith("bmp"))){
			return ImageCompressUtils.compressImage(is, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
		}
		else{
			return is;
		}
	}
}
