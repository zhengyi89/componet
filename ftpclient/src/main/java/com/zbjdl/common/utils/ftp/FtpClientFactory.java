package com.zbjdl.common.utils.ftp;

import com.zbjdl.common.utils.ftp.enums.FtpClientTypeEnum;
import com.zbjdl.common.utils.ftp.impl.FtpClientImpl;
import com.zbjdl.common.utils.ftp.impl.FtpsClientImpl;
import com.zbjdl.common.utils.ftp.impl.SftpClientImpl;

/**
 * ftp client 工厂
 * 
 */
public class FtpClientFactory {
	/**
	 * 获得FTP默认配置
	 * 
	 * @param ftpClientType
	 * @param ftpClientConfig
	 * @return
	 */
	public static FtpClient getFtpClient(FtpClientTypeEnum ftpClientType,
			FtpClientConfig ftpClientConfig) {
		if(ftpClientType==null){
			throw new RuntimeException("客户端类型不能为空！");
		}
		if(ftpClientConfig==null){
			throw new RuntimeException("ftp配置信息不能为空！！");
		}
		switch (ftpClientType) {
		case FTP:
			return new FtpClientImpl(ftpClientConfig);
		case SFTP:
			return new SftpClientImpl(ftpClientConfig);
		case FTPS:
			return new FtpsClientImpl(ftpClientConfig);
		}
		return null;
	}

	/**
	 * 获得FTP默认配置
	 * 
	 * @param ftpClientType
	 * @return
	 */
	public static FtpClient getFtpClient(FtpClientTypeEnum ftpClientType) {
		return getFtpClient(ftpClientType, FtpClientConfig.getDefaultConfig());
	}

}
