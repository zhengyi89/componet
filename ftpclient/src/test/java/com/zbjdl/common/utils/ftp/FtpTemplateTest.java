package com.zbjdl.common.utils.ftp;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import org.apache.log4j.lf5.util.StreamUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zbjdl.common.utils.ftp.FtpCallback;
import com.zbjdl.common.utils.ftp.FtpClient;
import com.zbjdl.common.utils.ftp.FtpTemplate;
import com.zbjdl.common.utils.ftp.enums.FtpClientTypeEnum;

/**
 * ftp模板方法
 * 
 */
public class FtpTemplateTest {
	private String url;
	private String userName;
	private String password;

	private String cdPath;

	@Before
	public void before() {
		this.url = "192.168.120.216";
		this.userName = "app";
		this.password = "75xuBOpgy";
		this.cdPath = "/home/app/";
	}

	@Test
	public void testExeFtpAction() {
		String ss = UUID.randomUUID().toString();
		String ftpContent = FtpTemplate.exeFtpAction(FtpClientTypeEnum.SFTP,
				url, userName, password, new FtpCallback<String>() {
					@Override
					public String call(FtpClient ftpClient, Object... args) {
						ftpClient.cd(cdPath);
						ftpClient.send(new ByteArrayInputStream(args[0]
								.toString().getBytes()), "aa.txt");
						try {
							byte[] data = StreamUtils.getBytes(ftpClient
									.getStream("aa.txt"));
							return new String(data);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				}, ss);
		Assert.assertEquals(ss, ftpContent);
	}
}
