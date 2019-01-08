package com.zbjdl.common.utils.ftp.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zbjdl.common.utils.ftp.FtpClientConfig;

/**
 * @author：wei.du
 * @since：2013-7-2 下午4:45:13
 * @version:
 */
public abstract class AbstractFtpClient {

	Log log = LogFactory.getLog(getClass());
	/**
	 * 配置信息
	 */
	protected FtpClientConfig ftpClientConfig;
	/**
	 * ftp服务地址
	 */
	protected String url;
	/**
	 * 端口号
	 */
	protected int port;
	/**
	 * 用户名
	 */
	protected String userName;
	/**
	 * 密码
	 */
	protected String password;

	/**
	 * 设置登录信息
	 * 
	 * @param url
	 * @param port
	 * @param userName
	 * @param password
	 */
	protected void setLoginInfo(String url, int port, String userName,
			String password) {
		this.url = url;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}
}
