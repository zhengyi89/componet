/** 
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay) 
 */
package com.zbjdl.common.utils.ftp.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

import com.zbjdl.common.utils.ftp.FtpClient;
import com.zbjdl.common.utils.ftp.FtpClientConfig;
import com.zbjdl.common.utils.ftp.FtpException;
import com.zbjdl.common.utils.ftp.FtpFile;

/**
 * ftp客户端实现
 * 
 */
public class FtpClientImpl extends AbstractFtpClient implements FtpClient {
	/**
	 * 使用apache的ftpClient实现
	 */
	protected FTPClient ftpClient;

	/**
	 * 
	 * 创建一个新的实例 FtpClientImpl.
	 * 
	 * @param ftpClientType
	 * @param ftpClientConfig
	 */
	public FtpClientImpl(FtpClientConfig ftpClientConfig) {
		this.ftpClientConfig = ftpClientConfig;
		ftpClient = new FTPClient();
		ftpClient.setBufferSize(ftpClientConfig.getBufferSize());
		ftpClient.setControlEncoding(ftpClientConfig.getControlEncoding());
		FTPClientConfig conf = new FTPClientConfig(ftpClientConfig.getSystem());
		conf.setServerLanguageCode(ftpClientConfig.getServerLang());
		ftpClient.configure(conf);
		if(ftpClientConfig.isCommandLog()){
			ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#connect(java.lang.String, int,
	 * java.lang.String, java.lang.String)
	 */
	public void connect(String url, int port, String userName, String password)
			throws FtpException {
		if (log.isInfoEnabled()) {
			log.info(String
					.format("ftp connect to %s as %s ...", url, userName));
		}
		setLoginInfo(url, port, userName, password);
		try {
			ftpClient.connect(url, port);
			ftpClient.login(userName, password);
			if (log.isInfoEnabled()) {
				log.info("connect success.");
			}
		} catch (Exception e) {
			log.error("connect fail", e);
			throw FtpException.CONNECT_EXCEPTION.newInstance(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#connect(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public void connect(String url, String userName, String password)
			throws FtpException {
		connect(url, 21, userName, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#ascii()
	 */
	public void ascii() throws FtpException {
		if (log.isInfoEnabled()) {
			log.info("ftp change mode to ascii ...");
		}
		try {
			ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
			if (log.isInfoEnabled()) {
				log.info("change mode success.");
			}
		} catch (Exception e) {
			log.error("change mode fail", e);
			throw FtpException.CONNECT_EXCEPTION.newInstance(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#binary()
	 */
	public void binary() throws FtpException {
		if (log.isInfoEnabled()) {
			log.info("ftp change mode to binary ...");
		}
		try {
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			if (log.isInfoEnabled()) {
				log.info("change mode success.");
			}
		} catch (Exception e) {
			log.error("change mode fail", e);
			throw FtpException.CONNECT_EXCEPTION.newInstance(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#cd(java.lang.String)
	 */
	public void cd(String path) throws FtpException {
		if (log.isInfoEnabled()) {
			log.info(String.format("ftp change dir to %s ...", path));
		}
		try {
			boolean result = ftpClient.changeWorkingDirectory(path);
			if (!result) {
				throw FtpException.CHANGE_DIR_EXCEPTION
						.newInstance("UNKNOW EXCEPTION,PORBABLY NO ACCESS AUTHORITY");
			}
			if (log.isInfoEnabled()) {
				log.info("change dir success.");
			}
		} catch (Exception e) {
			log.info("change dir fail", e);
			throw FtpException.CHANGE_DIR_EXCEPTION.newInstance(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#mkdir(java.lang.String)
	 */
	public void mkdir(String dir) throws FtpException {
		if (log.isInfoEnabled()) {
			log.info(String.format("ftp make dir  %s ...", dir));
		}
		try {
			boolean result = ftpClient.makeDirectory(dir);
			if (!result) {
				throw FtpException.MAKE_DIR_EXCEPTION
						.newInstance("UNKNOW EXCEPTION,PORBABLY NO CREATE AUTHORITY");
			}
			if (log.isInfoEnabled()) {
				log.info("make dir success.");
			}
		} catch (Exception e) {
			log.info("make dir fail", e);
			throw FtpException.MAKE_DIR_EXCEPTION.newInstance(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#del(java.lang.String)
	 */
	public void del(String path) throws FtpException {
		if (log.isInfoEnabled()) {
			log.info(String.format("ftp delete file  %s ...", path));
		}
		try {
			boolean result = ftpClient.deleteFile(path);
			if (!result) {
				throw FtpException.DELETE_FILE_EXCEPTION
						.newInstance("UNKNOW EXCEPTION,PORBABLY NO DELETE AUTHORITY");
			}
			if (log.isInfoEnabled()) {
				log.info("delete file success.");
			}
		} catch (Exception e) {
			log.info("delete file fail", e);
			throw FtpException.DELETE_FILE_EXCEPTION.newInstance(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#ls(java.lang.String)
	 */
	public List<FtpFile> ls(String path) throws FtpException {
		List<FtpFile> fileList = new ArrayList<FtpFile>();
		try {
			FTPFile[] ftpFileArr = ftpClient.listFiles(path);
			for (FTPFile file : ftpFileArr) {
				FtpFile ftpFile = new FtpFile();
				ftpFile.setName(file.getName());
				ftpFile.setDir(file.isDirectory());
				ftpFile.setAddTime(file.getTimestamp().getTime());
				ftpFile.setModifyTime(file.getTimestamp().getTime());
				ftpFile.setDetailInfo(file.toFormattedString());
				ftpFile.setSize(file.getSize());
				fileList.add(ftpFile);
			}
		} catch (Exception e) {
			log.info("list file fail", e);
			// 如果无法展示，肯定是超时了
			throw FtpException.CONNECTION_TIMEOUT_EXCEPTION.newInstance(e);
		}
		return fileList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#send(java.lang.String,
	 * java.lang.String)
	 */
	public void send(String filePath, String targetFilePath)
			throws FtpException {
		if (log.isInfoEnabled()) {
			log.info(String.format("ftp send file  %s to %s ...", filePath,
					targetFilePath));
		}
		try {
			File file = new File(filePath);
			if (targetFilePath == null || targetFilePath.trim().length() < 1) {
				targetFilePath = file.getName();
			}
			boolean result = ftpClient.storeFile(targetFilePath,
					new FileInputStream(file));
			if (!result) {
				throw FtpException.UPLOAD_FILE_EXCEPTION
						.newInstance("UNKNOW EXCEPTION,PORBABLY NO WRITE AUTHORITY");
			}
			if (log.isInfoEnabled()) {
				log.info("send file success.");
			}
		} catch (Exception e) {
			log.info("send file fail", e);
			throw FtpException.UPLOAD_FILE_EXCEPTION.newInstance(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#send(java.lang.String)
	 */
	public void send(String filePath) throws FtpException {
		send(filePath, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#send(java.io.InputStream,
	 * java.lang.String)
	 */
	public void send(InputStream is, String targetFilePath) throws FtpException {
		if (log.isInfoEnabled()) {
			log.info(String.format("ftp send stream to %s ...", targetFilePath));
		}
		try {
			boolean result = ftpClient.storeFile(targetFilePath, is);
			if (!result) {
				throw FtpException.UPLOAD_FILE_EXCEPTION
						.newInstance("UNKNOW EXCEPTION,PORBABLY NO WRITE AUTHORITY");
			}
			if (log.isInfoEnabled()) {
				log.info("send file success.");
			}
		} catch (Exception e) {
			log.info("send file fail", e);
			throw FtpException.UPLOAD_FILE_EXCEPTION.newInstance(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#get(java.lang.String,
	 * java.lang.String)
	 */
	public void get(String filePath, String targetFilePath) throws FtpException {
		if (log.isInfoEnabled()) {
			log.info(String.format("ftp get remote file  %s to %s ...",
					filePath, targetFilePath));
		}
		if (targetFilePath == null || targetFilePath.trim().length() < 1) {
			targetFilePath = filePath.lastIndexOf("/") != -1 ? filePath
					.substring(filePath.lastIndexOf("/") + 1) : (filePath
					.lastIndexOf("\\") != -1 ? filePath.substring(filePath
					.lastIndexOf("\\") + 1) : filePath);
		}
		
		
		try {
			ftpClient.retrieveFile(filePath, new FileOutputStream(
					targetFilePath));
			if (log.isInfoEnabled()) {
				log.info("get remote file success.");
			}
		} catch (Exception e) {
			log.info("get remote file fail", e);
			throw FtpException.DOWNLOAD_FILE_EXCEPTION.newInstance(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#get(java.lang.String)
	 */
	public void get(String filePath) throws FtpException {
		get(filePath, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#getStream(java.lang.String)
	 */
	public InputStream getStream(String filePath) throws FtpException {
		if (log.isInfoEnabled()) {
			log.info(String.format(
					"ftp get remote file stream ,filepath: %s  ...", filePath));
		}
		try {
			InputStream is = ftpClient.retrieveFileStream(filePath);
			if (log.isInfoEnabled()) {
				log.info("get remote file success.");
			}
			return is;
		} catch (Exception e) {
			log.info("get remote file fail", e);
			throw FtpException.DOWNLOAD_FILE_EXCEPTION.newInstance(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#disconnect()
	 */
	public void disconnect() {
		if (log.isInfoEnabled()) {
			log.info("ftp exit...");
		}
		if (ftpClient.isConnected()) {
			try {
				ftpClient.disconnect();
				if (log.isInfoEnabled()) {
					log.info("ftp exit success.");
				}
			} catch (IOException e) {
				log.error("exit failed", e);
			}
		}
	}
	
	public void enterLocalPassiveMode(){
		ftpClient.enterLocalPassiveMode();
	}
	
	public void enterLocalActiveMode(){
		ftpClient.enterLocalActiveMode();
	}
	
}
