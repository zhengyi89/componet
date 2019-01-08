package com.zbjdl.common.utils.ftp.impl;

import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPSClient;

import com.zbjdl.common.utils.ftp.FtpClient;
import com.zbjdl.common.utils.ftp.FtpClientConfig;
import com.zbjdl.common.utils.ftp.FtpException;

/**
 * Ftps客户端
 */
public class FtpsClientImpl extends FtpClientImpl implements FtpClient {
	/**
	 * 
	 * 创建一个新的实例 FtpsClientImpl.
	 * 
	 * @param ftpClientConfig
	 */
	public FtpsClientImpl(FtpClientConfig ftpClientConfig) {
		super(ftpClientConfig);
		this.ftpClientConfig = ftpClientConfig;
		ftpClient = new FTPSClient(ftpClientConfig.getSftpProtocol());
		ftpClient.setBufferSize(ftpClientConfig.getBufferSize());
		ftpClient.setControlEncoding(ftpClientConfig.getControlEncoding());
		FTPClientConfig conf = new FTPClientConfig(ftpClientConfig.getSystem());
		conf.setServerLanguageCode(ftpClientConfig.getServerLang());
		ftpClient.configure(conf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zbjdl.common.utils.ftp.impl.FtpClientImpl#connect(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void connect(String url, String userName, String password)
			throws FtpException {
		connect(url, 22, userName, password);
	}

}
