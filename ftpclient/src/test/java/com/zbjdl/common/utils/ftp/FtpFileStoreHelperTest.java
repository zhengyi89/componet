package com.zbjdl.common.utils.ftp;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import com.zbjdl.common.utils.ftp.FtpFileStoreHelper;


public class FtpFileStoreHelperTest {
	@Test
	public void tetUpload(){
		//在 classpath 中取得 ftpfilestore_app.properties 属性文件 
		//参数依次是：文件对象，文件扩展名，服务器地址，用户名，密码
		FtpFileStoreHelper helper = FtpFileStoreHelper.getHelper("app");
		//上传图片文件
		String fileName = helper.upload(new File("/apps/data/test.jpg"), "jpg");
		//返回的结果是一个随机生成的文件路径
		System.out.println(fileName);
		
	}

	
	
}
