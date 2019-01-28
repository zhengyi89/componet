package com.zbjdl.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileContentUtil {
	private static final Logger logger = LoggerFactory.getLogger(FileContentUtil.class);
	
	public static final String readFileContent(String fileFullName) {
		File file = new File(fileFullName);
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader bf = new BufferedReader(new FileReader(file));
			String content = "";
			while (content != null) {
				content = bf.readLine();
				if (content == null) {
					break;
				}
				sb.append(content.trim());
			}
			bf.close();
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return sb.toString();
	}
}
