
package com.zbjdl.common.log;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zbjdl.common.annotation.LogTable;
import com.zbjdl.common.utils.ReflectionUtils;
import com.zbjdl.common.utils.StringUtils;

public class SoaLogUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(SoaLogUtils.class);

	private static Object logSender;

	public static void save(Object tableObj) {
		save(SoaLogUtils.class.getName(), tableObj);
	}

	public static void save(String bizMessage) {
		save(SoaLogUtils.class.getName(), bizMessage);
	}
	/**
	 * 保存业务日志
	 * @param loggerName  调用此方法的类名称，可以通过调用this.getClass().getName()来获得
	 * @param tableObj    业务日志值对象
	 */
	public static void save(String loggerName, Object tableObj) {
		if (tableObj == null) {
			return;
		}
		checkInited();
		if (logSender == null) {
			logger.info("logSender is not inited. bizLog(LogTable) : "
					+ ToStringBuilder.reflectionToString(tableObj,
							ToStringStyle.SHORT_PREFIX_STYLE));
			return;
		}
		if (tableObj instanceof List) {
			List<?> list = (List<?>) tableObj;
			boolean singleTable = false;
			for (Object obj : list) {
				if (StringUtils.isNotBlank(checkLogTable(obj))) {
					singleTable = true;
					break;
				}
			}
			if (singleTable) {
				for (Object obj : list) {
					if (obj instanceof String) {
						save(loggerName, (String) tableObj);
					} else {
						save(loggerName, obj);
					}
				}
				return;
			}
		}
		String tableName = checkLogTable(tableObj);
		if (StringUtils.isNotBlank(tableName)) {
			try {
				Map<String, Object> columns = LogContentBuilder.build(tableObj);
				ReflectionUtils.executeMethod(logSender, "save", new Object[] {
						loggerName, tableName, columns }, new Class[] {
						String.class, String.class, Map.class });
			} catch (Throwable t) {
				logger.info("save log error. errMsg : "
						+ t.getMessage()
						+ " bizLog(LogTable) : "
						+ ToStringBuilder.reflectionToString(tableObj,
								ToStringStyle.SHORT_PREFIX_STYLE));
			}
		} else {
			save(loggerName,
					ToStringBuilder
							.reflectionToString(tableObj,
									ToStringStyle.SHORT_PREFIX_STYLE)
							.replace("\n", " ").replace("\t", "  "));
		}
	}
	
	/**
	 * 保存业务日志
	 * @param loggerName  调用此方法的类名称，可以通过调用this.getClass().getName()来获得
	 * @param bizMessage  业务日志内容字符串
	 */
	public static void save(String loggerName, String bizMessage) {
		if (StringUtils.isBlank(bizMessage)) {
			return;
		}
		checkInited();
		if (logSender == null) {
			logger.info("logSender is not inited. bizLog : " + bizMessage);
			return;
		}
		try {
			ReflectionUtils.executeMethod(logSender, "save", new Object[] {
					loggerName, bizMessage }, new Class[] { String.class,
					String.class });
		} catch (Throwable t) {
			logger.info("save log error. errMsg : " + t.getMessage()
					+ " bizLog :" + bizMessage);
		}
	}

	/**
	 * 检查该日志消息是否要记录到独立数据库表
	 *
	 * @param clazz
	 * @return
	 */
	private static String checkLogTable(Object message) {
		if (message == null) {
			return null;
		}
		Class<?> clazz = message.getClass();
		LogTable annotation = clazz.getAnnotation(LogTable.class);
		if (annotation != null) {
			return annotation.tableName().toUpperCase();
		}
		return null;
	}

	private static void checkInited() {
		if (logSender != null) {
			return;
		}
		try {
			synchronized (SoaLogUtils.class) {
				if (logSender == null) {
					Class<?> cl = Class
							.forName("com.zbjdl.common.soa.log.BizLoggerExecutor");
					Method m = cl.getDeclaredMethod("getInstance");
					logSender = m.invoke(null);
				}
			}
		} catch (Throwable t) {
			logger.error("init logSender error " + t.getClass().getName(), t);
		}
	}
}
