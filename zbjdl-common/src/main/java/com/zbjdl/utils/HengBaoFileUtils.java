/**
 * 
 */
package com.zbjdl.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件工具类
 * 
 * 
 * 
 */
public class HengBaoFileUtils {

	private BufferedWriter dos = null;

	private String fileName;

	private Logger logger = LoggerFactory.getLogger(HengBaoFileUtils.class);

	public HengBaoFileUtils(String processFile) {

		super();
		this.fileName = processFile;
		try {
			FileOutputStream fos = new FileOutputStream(processFile);
			dos = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 写入字符串
	 * 
	 * @param s
	 * @throws IOException
	 */
	public void writeString(String s) throws IOException {
		try {
			dos.write(s);
		} catch (Exception e) {
			// TODO: handle exception
			dos.write("");
		}
	}

	/**
	 * 写入分隔符
	 * @param sperator
	 * @throws IOException
	 */
	public void writeSeperator(String sperator) throws IOException {
		dos.write(sperator);
	}

	/**
	 * 写入换行符
	 * 
	 * @throws IOException
	 */
	public void writeLn() throws IOException {
		dos.write("\n");
	}

	/**
	 * 关闭文件流
	 * 
	 * @throws IOException
	 */
	public void finishWrite() {
		try {
			dos.close();
			logger.info("完成文件: " + fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
	}
}
