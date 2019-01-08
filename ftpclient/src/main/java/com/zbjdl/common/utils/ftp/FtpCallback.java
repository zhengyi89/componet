package com.zbjdl.common.utils.ftp;

/**
 * ftp回调函数
 * 
 */
public interface FtpCallback<T> {
	/**
	 * ftp行为
	 * 
	 * @param ftpClient
	 * @param args
	 * @return
	 */
	public T call(FtpClient ftpClient, Object... args);
}
