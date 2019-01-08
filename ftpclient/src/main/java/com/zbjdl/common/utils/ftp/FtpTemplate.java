package com.zbjdl.common.utils.ftp;

import com.zbjdl.common.utils.ftp.enums.FtpClientTypeEnum;

/**
 * ftp模板方法
 * 
 * @author：wei.du
 * @since：2014年12月25日 下午5:49:57
 * @version:
 */
public class FtpTemplate {
	/**
	 * 执行FTP操作
	 * 
	 * @param url
	 *            ：地址
	 * @param user
	 *            ：用户名
	 * @param password
	 *            ：密码
	 * @param ftpCallBack
	 *            ：回调函数
	 * @param args
	 *            ：回调时的参数
	 * @return
	 */
	public static <T> T exeFtpAction(String url, String user, String password,
			FtpCallback<T> ftpCallBack, Object... args) {
		return exeFtpAction(FtpClientTypeEnum.FTP, url, user, password,
				ftpCallBack, args);
	}

	/**
	 * 执行FTP操作
	 * 
	 * @param ftpClientType
	 *            ：ftp类型
	 * @param url
	 *            ：地址
	 * @param user
	 *            ：用户名
	 * @param password
	 *            ：密码
	 * @param ftpCallBack
	 *            ：回调函数
	 * @param args
	 *            ：回调时的参数
	 * @return
	 */
	public static <T> T exeFtpAction(FtpClientTypeEnum ftpClientType,
			String url, String user, String password,
			FtpCallback<T> ftpCallBack, Object... args) {
		return exeFtpAction(ftpClientType, url, ftpClientType.getDefaultPort(),
				user, password, ftpCallBack, args);

	}

	/**
	 * 执行FTP操作
	 * 
	 * @param ftpClientType
	 *            ：ftp类型
	 * @param url
	 *            ：地址
	 * @param port
	 *            ：端口
	 * @param user
	 *            ：用户名
	 * @param password
	 *            ：密码
	 * @param ftpCallBack
	 *            ：回调函数
	 * @param args
	 *            ：回调时的参数
	 * @return
	 */
	public static <T> T exeFtpAction(FtpClientTypeEnum ftpClientType,
			String url, int port, String user, String password,
			FtpCallback<T> ftpCallBack, Object... args) {
		return exeFtpAction(ftpClientType, null, url, port, user, password,
				ftpCallBack, args);

	}

	/**
	 * 执行FTP操作
	 * 
	 * @param ftpClientType
	 *            ：ftp类型
	 * @param ftpClientConfig
	 *            ：ftp配置
	 * @param url
	 *            ：地址
	 * @param port
	 *            ：端口
	 * @param user
	 *            ：用户名
	 * @param password
	 *            ：密码
	 * @param ftpCallBack
	 *            ：回调函数
	 * @param args
	 *            ：回调时的参数
	 * @return
	 */
	public static <T> T exeFtpAction(FtpClientTypeEnum ftpClientType,
			FtpClientConfig ftpClientConfig, String url, int port, String user,
			String password, FtpCallback<T> ftpCallBack, Object... args) {
		if (ftpClientType == null) {
			throw new RuntimeException("客户端类型不能为空！");
		}
		FtpClient ftpClient = ftpClientConfig == null ? FtpClientFactory
				.getFtpClient(ftpClientType) : FtpClientFactory.getFtpClient(
				ftpClientType, ftpClientConfig);
		try {
			ftpClient.connect(url, port, user, password);
			return ftpCallBack.call(ftpClient, args);
		} finally {
			ftpClient.disconnect();
		}
	}
}
