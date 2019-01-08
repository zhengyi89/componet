package com.zbjdl.common.utils.ftp.impl;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.zbjdl.common.utils.ftp.FtpClient;
import com.zbjdl.common.utils.ftp.FtpClientConfig;
import com.zbjdl.common.utils.ftp.FtpException;
import com.zbjdl.common.utils.ftp.FtpFile;

/**
 * Sftp客户端
 * 
 */
public class SftpClientImpl extends AbstractFtpClient implements FtpClient {
	public SftpClientImpl(FtpClientConfig ftpClientConfig) {
		this.ftpClientConfig = ftpClientConfig;
	}

	private ChannelSftp sftp;

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
			JSch jsch = new JSch();
			jsch.getSession(userName, url, port);
			Session sshSession = jsch.getSession(userName, url, port);
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
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
		connect(url, 22, userName, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#ascii()
	 */
	public void ascii() throws FtpException {
		if (log.isInfoEnabled()) {
			log.info("ftp change mode to ascii ...");
			log.info("sftp no need change.");
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
			log.info("sftp no need change.");
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
			sftp.cd(path);
			if (log.isInfoEnabled()) {
				log.info("change dir success.");
			}
		} catch (Exception e) {
			log.info("change dir fail", e);
			throw FtpException.CHANGE_DIR_EXCEPTION.newInstance(e);
		}
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.FtpClient#mkdir(java.lang.String)
	 */
	
	public void mkdir(String dir) throws FtpException {
		if (log.isInfoEnabled()) {
			log.info(String.format("ftp make dir  %s ...", dir));
		}
		try {
			sftp.mkdir(dir);
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
			sftp.rm(path);
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
			@SuppressWarnings("unchecked")
			Vector<LsEntry> rs = sftp.ls(path);
			for (LsEntry file : rs) {
				FtpFile ftpFile = new FtpFile();
				ftpFile.setName(file.getFilename());
				ftpFile.setDir(file.getAttrs().isDir());
				ftpFile.setAddTime(new Date(file.getAttrs().getATime()));
				ftpFile.setModifyTime(new Date((long) file.getAttrs()
						.getMTime() * 1000L));
				ftpFile.setDetailInfo(file.getAttrs().toString());
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
			sftp.put(filePath, targetFilePath);
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
			sftp.put(is, targetFilePath);
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
			sftp.get(filePath, targetFilePath);
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

	/* (non-Javadoc)    
	 * @see com.zbjdl.common.utils.ftp.FtpClient#getStream(java.lang.String)    
	 */
	
	public InputStream getStream(String filePath) throws FtpException {
		if (log.isInfoEnabled()) {
			log.info(String.format(
					"ftp get remote file stream ,filepath: %s  ...", filePath));
		}
		try {
			InputStream is = sftp.get(filePath);
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
		if (sftp.isConnected()) {
			try {
				sftp.disconnect();
				if (log.isInfoEnabled()) {
					log.info("ftp exit success.");
				}
			} catch (Exception e) {
				log.error("exit failed", e);
			}
		}
	}

	/* (non-Javadoc)    
	 * @see com.zbjdl.common.utils.ftp.FtpClient#enterLocalPassiveMode()    
	 */
	@Override
	public void enterLocalPassiveMode() {
		// default do nothing
	}

	/* (non-Javadoc)    
	 * @see com.zbjdl.common.utils.ftp.FtpClient#enterLocalActiveMode()    
	 */
	@Override
	public void enterLocalActiveMode() {
		// default do nothing
	}

}
