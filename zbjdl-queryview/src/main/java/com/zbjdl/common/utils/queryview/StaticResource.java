/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @since：2012-5-20 下午05:25:45
 * @version:
 */
public class StaticResource {

	protected static final Logger logger = LoggerFactory.getLogger(StaticResource.class);

	private static Map<String, StringBuffer> staticResources = new HashMap<String, StringBuffer>();
	private static final String RESOURCE_PACKAGE = "com/zbjdl/common/utils/queryview/";
	private static final String RESOURCE_ENCODING = "UTF-8";

	public static String getStaticHtml(String fileName) {
		String templatePath = RESOURCE_PACKAGE + fileName;
		if (staticResources.containsKey(templatePath)) {
			return staticResources.get(templatePath).toString();
		}
		InputStream template = Thread.currentThread().getContextClassLoader().getResourceAsStream(templatePath);
		Reader reader = null;
		try {
			reader = new InputStreamReader(template, RESOURCE_ENCODING);
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(reader);
			String tmp = null;
			while ((tmp = br.readLine()) != null) {
				sb.append(tmp);
				sb.append("\r");
			}
			staticResources.put(templatePath, sb);
			return sb.toString();
		} catch (Exception e) {
			throw new QueryUIException("read static resource fail", e);
		} finally {
			try {
				template.close();
				reader.close();
			} catch (Throwable e) {
			}
		}
	}
}
