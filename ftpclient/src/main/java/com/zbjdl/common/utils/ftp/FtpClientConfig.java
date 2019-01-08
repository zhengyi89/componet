package com.zbjdl.common.utils.ftp;

import java.util.ResourceBundle;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ftp 客户端配置
 * 
 */
public class FtpClientConfig {
	/**
	 * 服务端语言
	 */
	private String serverLang;
	/**
	 * 控制语言编码
	 */
	private String controlEncoding;
	/**
	 * 系统类型
	 */
	private String system;
	/**
	 * 缓冲区大小
	 */
	private int bufferSize;
	/**
	 * sftp加密协议
	 */
	private String sftpProtocol;
	
	private boolean commandLog;

	/**
	 * 获取默认配置
	 */
	public static FtpClientConfig getDefaultConfig() {
		ResourceBundle configResourceBundle = ResourceBundle
				.getBundle("ftp_client_default_config");
		FtpClientConfig ftpClientConfig = new FtpClientConfig();
		ftpClientConfig.setControlEncoding(configResourceBundle
				.getString("controlEncoding"));
		ftpClientConfig.setBufferSize(Integer.parseInt(configResourceBundle
				.getString("bufferSize")));
		ftpClientConfig.setSystem(configResourceBundle.getString("system"));
		ftpClientConfig.setServerLang(configResourceBundle
				.getString("serverLang"));
		ftpClientConfig.setSftpProtocol(configResourceBundle
				.getString("sftp_protocol"));
		return ftpClientConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * serverLang
	 * 
	 * @return the serverLang
	 */

	public String getServerLang() {
		return serverLang;
	}

	/**
	 * @param serverLang
	 *            the serverLang to set
	 */
	public void setServerLang(String serverLang) {
		this.serverLang = serverLang;
	}

	/**
	 * controlEncoding
	 * 
	 * @return the controlEncoding
	 */

	public String getControlEncoding() {
		return controlEncoding;
	}

	/**
	 * @param controlEncoding
	 *            the controlEncoding to set
	 */
	public void setControlEncoding(String controlEncoding) {
		this.controlEncoding = controlEncoding;
	}

	/**
	 * system
	 * 
	 * @return the system
	 */

	public String getSystem() {
		return system;
	}

	/**
	 * @param system
	 *            the system to set
	 */
	public void setSystem(String system) {
		this.system = system;
	}

	/**
	 * bufferSize
	 * 
	 * @return the bufferSize
	 */

	public int getBufferSize() {
		return bufferSize;
	}

	/**
	 * @param bufferSize
	 *            the bufferSize to set
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * sftpProtocol
	 * 
	 * @return the sftpProtocol
	 */

	public String getSftpProtocol() {
		return sftpProtocol;
	}

	/**
	 * @param sftpProtocol
	 *            the sftpProtocol to set
	 */
	public void setSftpProtocol(String sftpProtocol) {
		this.sftpProtocol = sftpProtocol;
	}

	public boolean isCommandLog() {
		return commandLog;
	}

	public void setCommandLog(boolean commandLog) {
		this.commandLog = commandLog;
	}
}
