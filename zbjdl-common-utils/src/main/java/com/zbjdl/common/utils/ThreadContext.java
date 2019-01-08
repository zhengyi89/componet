package com.zbjdl.common.utils;

import java.util.HashMap;
import java.util.Map;


/**
 * 线程上下文对象
 *
 * @author：feng
 * @since：2011-2-25 下午05:59:57
 * @version:
 */
public class ThreadContext {
	/**
	 * 应用名
	 */
	private String appName;

	/**
	 * 线程请求唯一号(和线程号不同，每次运行都产生一个唯一号)
	 */
	private String threadUID;

	/**
	 * 线程请求唯一号(和线程号不同，每次运行都产生一个唯一号)
	 */
	private String newThreadUID;

	/**
	 * 线程启动时间
	 */
	private long threadStartTime;
	/**
	 * 线程类型
	 */
	private ThreadContextType type;
	/**
	 * 线程绑定参数
	 */
	private Map<String, Object> threadValues;

	/**
	 *
	 * 创建一个新的实例 ThreadContext.
	 *
	 * @param a_sourceUID
	 *            外部线程传递过来的唯一号
	 * @param a_type
	 *            线程类型
	 */
	@Deprecated
	public ThreadContext(String a_sourceUID, ThreadContextType a_type) {
		if (a_sourceUID == null || a_sourceUID.trim().length() == 0) {
			this.threadUID = CommonUtils.getUUID();
		} else {
			this.threadUID = a_sourceUID;
			this.newThreadUID = CommonUtils.getUUID();
		}
		this.type = a_type;
	}

	/**
	 *
	 * 创建一个新的实例 ThreadContext.
	 *
	 * @param a_sourceUID
	 *            外部线程传递过来的唯一号
	 * @param a_type
	 *            线程类型
	 */
	public ThreadContext(String appName, String a_sourceUID, ThreadContextType a_type) {
		this.appName = appName;
		if (a_sourceUID == null || a_sourceUID.trim().length() == 0) {
			this.threadUID = CommonUtils.getUUID();
		} else {
			this.threadUID = a_sourceUID;
			this.newThreadUID = CommonUtils.getUUID();
		}
		this.type = a_type;
	}

	/**
	 * 添加线程参数
	 *
	 * @param key
	 * @param value
	 */
	public void addValue(String key, Object value) {
		if (threadValues == null) {
			threadValues = new HashMap<String, Object>();
		}
		threadValues.put(key, value);
	}

	/**
	 * 获取线程参数
	 *
	 * @param key
	 * @return
	 */
	public Object getValue(String key) {
		return threadValues == null ? null : threadValues.get(key);
	}

	public String getAppName() {
		return appName;
	}

	public String getThreadUID() {
		return threadUID;
	}

	public String getNewThreadUID() {
		return newThreadUID;
	}

	public long getThreadStartTime() {
		return threadStartTime;
	}

	public ThreadContextType getType() {
		return type;
	}

	/**
	 * 清除单个线程参数
	 *
	 * @param key
	 */
	public void removeValue(String key) {
		if (threadValues != null) {
			threadValues.remove(key);
		}
	}

	/**
	 * 清除所有线程参数
	 */
	public void removeValue() {
		if (threadValues != null) {
			threadValues.clear();
		}
	}
}
