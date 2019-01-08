package com.zbjdl.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 线程环境变量工具
 *
 * @author：feng
 * @version:
 */
public class ThreadContextUtils {
	public static final String KEY_GUID = "_thread_uid";

	private static Map<ClassLoader, String> appNameCache = new HashMap<ClassLoader, String>();

	/**
	 * 清空线程对象，线程结束前必须调用
	 */
	public static void clearContext() {
		threadContext.remove();
	}

	/**
	 * 初始化本地线程上下文变量
	 *
	 * @param sourceUID
	 * @param type
	 */
	@Deprecated
	public static void initContext(String sourceUID, ThreadContextType type) {
		initContext(null, sourceUID, type);
	}

	/**
	 * 初始化本地线程上下文变量
	 *
	 * @param sourceUID
	 * @param type
	 */
	public static void initContext(String appName, String sourceUID,
			ThreadContextType type) {
		// 如果已经设置过则抛出异常
		if (threadContext.get() != null) {
			throw new RuntimeException("thread context already initialized");
		}
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		String cachedAppName = appNameCache.get(cl);
		if (StringUtils.isBlank(appName)) {
			appName = cachedAppName;
		} else if (StringUtils.isBlank(cachedAppName)
				&& !StringUtils.equals(appName, "monitor")) {
			appNameCache.put(cl, appName);
		}
		ThreadContext context = new ThreadContext(appName, sourceUID, type);
		threadContext.set(context);
	}

	/**
	 * 判断本地线程变量是否已经初始化过
	 *
	 * @return
	 */
	public static boolean contextInitialized() {
		return threadContext.get() != null;
	}

	/**
	 * 获取线程上下文变量
	 *
	 * @return
	 */
	public static ThreadContext getContext() {
		return threadContext.get();
	}

	public static String getAppName() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return appNameCache.get(cl);
	}

	private static ThreadLocal<ThreadContext> threadContext = new ThreadLocal<ThreadContext>() {
		protected ThreadContext initialValue() {
			return null;
		}
	};
}
