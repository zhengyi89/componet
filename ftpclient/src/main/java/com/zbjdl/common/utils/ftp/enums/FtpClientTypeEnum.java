package com.zbjdl.common.utils.ftp.enums;

/**
 * ftp 客户端类型
 * 
 */
public enum FtpClientTypeEnum {
	FTP(21), SFTP(22), FTPS(21);
	/**
	 * 默认端口
	 */
	private int defaultPort;

	private FtpClientTypeEnum(int defaultPort) {
		this.defaultPort = defaultPort;
	}

	public int getDefaultPort() {
		return defaultPort;
	}

	public void setDefaultPort(int defaultPort) {
		this.defaultPort = defaultPort;
	}

}
