package com.zbjdl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFtpClientUtil {

	private final Logger logger = LoggerFactory.getLogger(SFtpClientUtil.class);
			
	public static void main(String[] args) {
		String host = "172.20.6.129";
		int port = 22;
		String username = "hengbao";
		String password = "123@hengbao";
		String directory = "test";
		String uploadFile = "C:\\uploads\\crebas.sql";
		String downloadFile = "upload.txt";
		String saveFile = "C:\\uploads\\download.txt";
//		String deleteFile = "delete.txt";
		ChannelSftp sftp =(new SFtpClientUtil()).connect(host, port, username, password);
		try {
			System.out.println(sftp.pwd());
			System.out.println(sftp.pwd());
		} catch (SftpException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		(new SFtpClientUtil()).upload(directory, uploadFile, sftp);
		(new SFtpClientUtil()).download(directory, downloadFile, saveFile, sftp);
//		SFtpClientUtil.delete(directory, deleteFile, sftp);
		try {
			System.out.println(sftp.pwd());
			sftp.mkdir("ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
			
			
	/**
	 * 连接sftp服务器
	 * 
	 * @param host 主机
	 * @param port 端口
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 */
	public ChannelSftp connect(String host, int port, String username, String password) {
		ChannelSftp sftp = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			Session sshSession = jsch.getSession(username, host, port);
			logger.info("Session created.");
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			logger.info("Session connected.");
			logger.info("Opening Channel.");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			logger.info("Connected to " + host + ".");
		} catch (Exception e) {
			logger.error(""+e);
		}
		return sftp;
	}

	/**
	 * 上传文件
	 * 
	 * @param directory 上传的目录
	 * @param uploadFile 要上传的文件
	 * @param sftp
	 */
	public void upload(String directory, String uploadFile, ChannelSftp sftp) {
		try {
			System.out.println(sftp.pwd());
			sftp.cd(directory);
			System.out.println(sftp.pwd());
			sftp.cd("..");
			System.out.println(sftp.pwd());
			File file = new File(uploadFile);
			sftp.put(new FileInputStream(file), file.getName());
		} catch (Exception e) {
			logger.error("" + e);
		}
	}

	/**
	 * 下载文件
	 * @param directory 下载目录
	 * @param downloadFile 下载的文件
	 * @param saveFile 存在本地的路径
	 * @param sftp
	 */
	public void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			File file = new File(saveFile);
			sftp.get(downloadFile, new FileOutputStream(file));
		} catch (Exception e) {
			logger.error("" + e);
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param directory 要删除文件所在目录
	 * @param deleteFile 要删除的文件
	 * @param sftp
	 */
	public void delete(String directory, String deleteFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			sftp.rm(deleteFile);
		} catch (Exception e) {
			logger.error("" + e);
		}
	}

	/**
	 * 列出目录下的文件
	 * 
	 * @param directory 要列出的目录
	 * @param sftp
	 * @return
	 * @throws SftpException
	 */
	public Vector<LsEntry> listFiles(String directory, ChannelSftp sftp) throws SftpException {
		return sftp.ls(directory);
	}
}
