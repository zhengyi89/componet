package com.zbjdl.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StrUtil {

	/**
	 * 字符串转换
	 * 
	 * @param value
	 * @return
	 */
	public static String transToString(Object value) {
		String result = "";
		if (value == null)
			return result;

		if (value instanceof String) {
			return (String) value;
		}

		if (value instanceof Integer) {
			return String.valueOf((Integer) value);
		}

		if (value instanceof Long) {
			return String.valueOf((Long) value);
		}

		if (value instanceof Date) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:SS")
					.format((Date) value);
		}

		if (value instanceof Double) {
			return String.valueOf((Double) value);
		}

		return result;
	}
	
	/**
	 * 字符串转码 - 解决乱码问题
	 * 
	 * @param str
	 * @param strCode
	 *            编码
	 * @return
	 */
	public static String getBytesForU8(String str, String strCode) {
		try {
			return new String(str.getBytes(strCode), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
}
