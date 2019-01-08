package com.zbjdl.common.utils.ftp;

import java.io.InputStream;
import java.util.List;

/**
 * Ftp客户端
 * 
 */
public interface FtpClient {
	/**
	 * 连接ftp服务
	 * 
	 * @param url
	 * @param port
	 * @param userName
	 * @param password
	 * @throws FtpException
	 */
	public void connect(String url, int port, String userName, String password)
			throws FtpException;

	/**
	 * 连接ftp服务
	 * 
	 * @param url
	 * @param userName
	 * @param password
	 * @throws FtpException
	 */
	public void connect(String url, String userName, String password)
			throws FtpException;

	/**
	 * ascii 传输
	 * 
	 * @throws FtpException
	 */
	public void ascii() throws FtpException;

	/**
	 * 二进制传输
	 * 
	 * @throws FtpException
	 */
	public void binary() throws FtpException;

	/**
	 * 改变目录
	 * 
	 * @param path
	 * @throws FtpException
	 */
	public void cd(String path) throws FtpException;

	/**
	 * 创建目录
	 * 
	 * @param dir
	 * @throws FtpException
	 */
	public void mkdir(String dir) throws FtpException;

	/**
	 * 删除文件或目录
	 * 
	 * @param path
	 * @throws FtpException
	 */
	public void del(String path) throws FtpException;

	/**
	 * 展示所有文件和目录
	 * 
	 * @param path
	 * @return
	 * @throws FtpException
	 */
	public List<FtpFile> ls(String path) throws FtpException;

	/**
	 * 上传文件到目标地址
	 * 
	 * @param filePath
	 * @param targetFilePath
	 * @throws FtpException
	 */
	public void send(String filePath, String targetFilePath)
			throws FtpException;

	/**
	 * 直接按当前文件名上传
	 * 
	 * @param filePath
	 * @throws FtpException
	 */
	public void send(String filePath) throws FtpException;

	/**
	 * 上传文件流
	 * 
	 * @param is
	 * @param targetFilePath
	 */
	public void send(InputStream is, String targetFilePath) throws FtpException;

	/**
	 * 下载文件到目标地址
	 * 
	 * @param filePath
	 * @param targetFilePath
	 * @throws FtpException
	 */
	public void get(String filePath, String targetFilePath) throws FtpException;

	/**
	 * 按远程文件名下载文件
	 * 
	 * @param filePath
	 * @throws FtpException
	 */
	public void get(String filePath) throws FtpException;

	/**
	 * 获得远程文件
	 * @param filePath
	 * @return
	 * @throws FtpException
	 */
	public InputStream getStream(String filePath) throws FtpException;

	/**
	 * 关闭连接
	 */
	public void disconnect();

	/**
	 * 设置客户端为Passive模式
	 */
	public void enterLocalPassiveMode();
	
	/**
	 * 设置客户端为active 模式
	 */
	public void enterLocalActiveMode();
}
