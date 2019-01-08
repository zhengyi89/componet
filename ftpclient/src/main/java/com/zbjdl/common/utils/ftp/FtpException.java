package com.zbjdl.common.utils.ftp;

import java.text.MessageFormat;

/**
 * ftp异常
 * 
 */
public class FtpException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	/**
	 * 连接异常
	 */
	public static FtpException CONNECT_EXCEPTION = new FtpException("100000",
			"CONNECT EXCEPTION");
	/**
	 * 改变目录异常
	 */
	public static FtpException CHANGE_DIR_EXCEPTION = new FtpException(
			"100001", "CHANGE DIRECTROY EXCEPTION");
	/**
	 * 下载文件异常
	 */
	public static FtpException DOWNLOAD_FILE_EXCEPTION = new FtpException(
			"100002", "DWONLAOD FAILE EXCEPTION");
	/**
	 * 上传文件失败
	 */
	public static FtpException UPLOAD_FILE_EXCEPTION = new FtpException(
			"100003", "UPLOAD FILE EXCEPTION");
	/**
	 * 删除文件失败
	 */
	public static FtpException DELETE_FILE_EXCEPTION = new FtpException(
			"100004", "DELETE EXCEPTION");
	/**
	 * 连接超时
	 */
	public static FtpException CONNECTION_TIMEOUT_EXCEPTION = new FtpException(
			"100005", "CONNECTION TIMEOUT");

	public static FtpException MAKE_DIR_EXCEPTION = new FtpException("100006",
			"MAKE DIR EXCEPTION");
	
	/**
	 * 文件无效
	 */
	public static FtpException FILE_EXCEPTION = new FtpException("100007",
			"FILE EXCEPTION");

	/**
	 * 异常代码
	 */
	private String errCode;
	/**
	 * 异常摘要
	 */
	private String errSummary;

	/**
	 * 
	 * 创建一个新的实例 FtpException.
	 * 
	 * @param errCode
	 * @param errSummary
	 */

	public FtpException(String errCode, String errSummary) {
		super(new StringBuilder().append("Error Code:").append(errCode)
				.append(",Error Summary:").append(errSummary).toString());
		this.errCode = errCode;
		this.errSummary = errSummary;
	}

	/**
	 * 
	 * 创建一个新的实例 FtpException.
	 * 
	 * @param e
	 * @param errCode
	 * @param errSummary
	 */
	public FtpException(Exception e, String errCode, String errSummary) {
		super(new StringBuilder().append("Error Code:").append(errCode)
				.append(",Error Summary:").append(errSummary).toString(), e);
		this.errCode = errCode;
		this.errSummary = errSummary;
	}

	/**
	 * 获得一个异常的实例
	 * 
	 * @param msg
	 * @param args
	 * @return
	 */
	public FtpException newInstance(String msg, Object... args) {
		String message = (msg != null && args != null) ? MessageFormat.format(
				msg, args) : msg;
		FtpException ftpException = new FtpException(this.errCode, message);
		return ftpException;
	}

	/**
	 * 获得一个异常的实例
	 * 
	 * @return
	 */
	public FtpException newInstance(Exception e) {
		FtpException ftpException = new FtpException(e, this.errCode,
				this.errSummary);
		return ftpException;
	}
}
