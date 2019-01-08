package com.zbjdl.common.utils.ftp;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.zbjdl.common.utils.ftp.FtpClient;
import com.zbjdl.common.utils.ftp.FtpClientFactory;
import com.zbjdl.common.utils.ftp.FtpException;
import com.zbjdl.common.utils.ftp.enums.FtpClientTypeEnum;


public class FtpClientTest {

	private FtpClient ftpClient;

	private String url;
	private String userName;
	private String password;

	private String sendFilePath;

	private String targetFilePath;

	private String cdPath;

	@Before
	public void before() {
		ftpClient = FtpClientFactory.getFtpClient(FtpClientTypeEnum.SFTP);
		 this.url = "192.168.120.216";
		 this.userName = "app";
		 this.password = "75xuBOpgy";
		this.sendFilePath = "/apps/data/test.jpg";
		this.targetFilePath = "/data/test.jpg";
		this.cdPath = "/";
	}

	/**
	 * Test method for
	 * {@link com.zbjdl.common.utils.ftp.impl.FtpClientImpl#connect(java.lang.String, int, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testConnect() {
		try {
			ftpClient.connect(url, userName, password);
			ftpClient.disconnect();
		} catch (FtpException e) {
			e.printStackTrace();
			fail("连接失败");
		}
	}

	/**
	 * Test method for {@link com.zbjdl.common.utils.ftp.impl.FtpClientImpl#ascii()}.
	 */
	@Test
	public void testAscii() {
		try {
			ftpClient.connect(url, userName, password);
			ftpClient.ascii();
			ftpClient.disconnect();
		} catch (FtpException e) {
			e.printStackTrace();
			fail("连接失败");
		}
	}

	/**
	 * Test method for {@link com.zbjdl.common.utils.ftp.impl.FtpClientImpl#binary()}.
	 */
	@Test
	public void testBinary() {
		try {
			ftpClient.connect(url, userName, password);
			ftpClient.binary();
			ftpClient.disconnect();
		} catch (FtpException e) {
			e.printStackTrace();
			fail("连接失败");
		}
	}

	/**
	 * Test method for
	 * {@link com.zbjdl.common.utils.ftp.impl.FtpClientImpl#cd(java.lang.String)}.
	 */
	@Test
	public void testCd() {
		try {
			ftpClient.connect(url, userName, password);
			ftpClient.cd(cdPath);
			ftpClient.disconnect();
		} catch (FtpException e) {
			e.printStackTrace();
			fail("连接失败");
		}
	}

	/**
	 * Test method for
	 * {@link com.zbjdl.common.utils.ftp.impl.FtpClientImpl#del(java.lang.String)}.
	 */
	@Test
	public void testDel() {
		try {
			ftpClient.connect(url, userName, password);
			ftpClient.send(sendFilePath);
			ftpClient.del(sendFilePath.lastIndexOf("/") != -1 ? sendFilePath
					.substring(sendFilePath.lastIndexOf("/") + 1)
					: sendFilePath);
			ftpClient.disconnect();
		} catch (FtpException e) {
			e.printStackTrace();
			fail("连接失败");
		}
	}

	/**
	 * Test method for {@link com.zbjdl.common.utils.ftp.impl.FtpClientImpl#ls()}.
	 */
	@Test
	public void testLs() {
		try {
			ftpClient.connect(url, userName, password);
			System.out.println(ftpClient.ls(cdPath));
			ftpClient.disconnect();
		} catch (FtpException e) {
			e.printStackTrace();
			fail("连接失败");
		}
	}

	/**
	 * Test method for
	 * {@link com.zbjdl.common.utils.ftp.impl.FtpClientImpl#send(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testSend() {
		try {
			ftpClient.connect(url, userName, password);
			ftpClient.send(sendFilePath, targetFilePath);
//			try {
//				ftpClient.send(new FileInputStream("D:/paymentrule.sql"),
//						"paymentrule.sql");
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			ftpClient.disconnect();
		} catch (FtpException e) {
			e.printStackTrace();
			fail("连接失败");
		}
	}

	/**
	 * Test method for
	 * {@link com.zbjdl.common.utils.ftp.impl.FtpClientImpl#get(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGet() {
		try {
			ftpClient.connect(url, userName, password);
//			ftpClient.send("d:/460.gif");
			InputStream is = ftpClient.getStream("/data/test.jpg");
//			InputStream is = ftpClient.getStream("test.jpg");
			FileOutputStream fos = new FileOutputStream("/apps/down.jpg");
			byte[]buf =new byte[1024];
			for(int n=0;(n=is.read(buf))>0;){
				
				fos.write(buf, 0, n);
			}
			ftpClient.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			fail("连接失败");
		}
	}

	/**
	 * Test method for
	 * {@link com.zbjdl.common.utils.ftp.impl.FtpClientImpl#disconnect()}.
	 */
	@Test
	public void testDisconnect() {
		try {
			ftpClient.connect(url, userName, password);
			ftpClient.disconnect();
		} catch (FtpException e) {
			e.printStackTrace();
			fail("连接失败");
		}
	}

	@Test
	public void testMakeDir() {
		try {
			ftpClient.connect(url, userName, password);
			ftpClient.mkdir("abc");
			ftpClient.disconnect();
		} catch (FtpException e) {
			e.printStackTrace();
			fail("连接失败");
		}
	}

}
