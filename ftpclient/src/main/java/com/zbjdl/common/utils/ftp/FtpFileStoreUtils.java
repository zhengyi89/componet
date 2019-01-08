package com.zbjdl.common.utils.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.zbjdl.common.utils.ftp.enums.FtpClientTypeEnum;

public class FtpFileStoreUtils {
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
	
	/**
	 * 把文件上传到服务器
	 * @param file 文件对象
	 * @param ip IP地址
	 * @param user 用户名
	 * @param password 密码
	 * @return 保存到服务器的文件路径和文件名
	 * @throws FileNotFoundException 
	 */
	public static String upload(File file,String fileExtension, String ip, String user, String password){
		return upload(file, fileExtension, ip,null, user, password);
	}
	
	public static String upload(File file,String fileExtension, String ip, Integer port, String user, String password){
		FileInputStream fi = null;;
		try {
			fi = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw FtpException.FILE_EXCEPTION.newInstance(e);
		}
		return upload(fi, fileExtension, ip,port, user, password);
	}
	
	public static String upload(InputStream is,String fileExtension, String ip, String user, String password){
		return upload(is, fileExtension, ip, null, user, password);
	}
	
	public static String upload(InputStream is,String fileExtension, String ip, Integer port, String user, String password){
		FtpClient ftpClient = FtpClientFactory.getFtpClient(FtpClientTypeEnum.FTP);
		//连接服务器
		try{
			if(port!=null){
				ftpClient.connect(ip, port, user, password);
			}else{
				ftpClient.connect(ip, user, password);
			}
			ftpClient.binary();
			// 根据当前日期生成目录
			String month = dateFormat.format(new Date());
			try {
				ftpClient.cd(month);
			} catch (FtpException e) {
				ftpClient.mkdir(month);
				ftpClient.cd(month);
			}
			String fileName = UUID.randomUUID().toString().replaceAll("-", "")
					+ "." + fileExtension;
			
			
			ftpClient.send(is, fileName);
			return month + "/" + fileName;
		}
		finally{
			try{
				ftpClient.disconnect();
			}
			catch(Exception e){
			}
			try{
				is.close();
			}
			catch(Exception e){
			}
		}
	}
	
	
	public static boolean uploadAndOverwrite(File file,String filePath, String ip, Integer port, String user, String password){
		FileInputStream fi = null;;
		try {
			fi = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw FtpException.FILE_EXCEPTION.newInstance(e);
		}
		return uploadAndOverwrite(fi, filePath, ip,port, user, password);
	}
	
	public static boolean uploadAndOverwrite(InputStream is,String filePath, String ip, Integer port, String user, String password){
		FtpClient ftpClient = FtpClientFactory.getFtpClient(FtpClientTypeEnum.FTP);
		//连接服务器
		try{
			if(port!=null){
				ftpClient.connect(ip, port, user, password);
			}else{
				ftpClient.connect(ip, user, password);
			}
			ftpClient.binary();
			
			// 根据当前日期生成目录
			ftpClient.send(is, filePath);
			return true;
		}catch(Exception e){
			return false;
		}
		finally{
			try{
				ftpClient.disconnect();
			}
			catch(Exception e){
			}
			try{
				is.close();
			}
			catch(Exception e){
			}
		}
	}
	
	public static void delete(String filePath, String ip, Integer port, String user, String password){
		FtpClient ftpClient = FtpClientFactory.getFtpClient(FtpClientTypeEnum.FTP);
		try{
			if(port!=null){
				ftpClient.connect(ip, port, user, password);
			}else{
				ftpClient.connect(ip, user, password);
			}
			ftpClient.del(filePath);
		}
		finally{
			try{
				ftpClient.disconnect();
			}
			catch(Exception e){
			}
		}
	}
	
	public static void delete(String filePath, String ip, String user, String password){
		delete(filePath, ip, null, user, password);
	}
	
	public static InputStream getStream(String filePath, String ip, Integer port, String user, String password){
		FtpClient ftpClient = FtpClientFactory.getFtpClient(FtpClientTypeEnum.FTP);
		try{
			if(port!=null){
				ftpClient.connect(ip, port, user, password);
			}else{
				ftpClient.connect(ip, user, password);
			}
			ftpClient.binary();
			return ftpClient.getStream(filePath);
		}
		finally{
			try{
				ftpClient.disconnect();
			}
			catch(Exception e){
			}
		}
	}
	
	public static InputStream getStream(String filePath, String ip, String user, String password){
		return getStream(filePath, ip, null, user, password);
	}
	
	
}
