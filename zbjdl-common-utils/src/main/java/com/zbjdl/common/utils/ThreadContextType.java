package com.zbjdl.common.utils;

/**
 * 线程类型枚举
 * @author：feng
 * @since：2011-2-25 下午06:33:59
 * @version:
 */
public enum ThreadContextType {
	/**
	 * 产品层WEB请求的处理线程
	 */
	WEB,
	/**
	 * 子系统接口请求的处理线程
	 */
	INTERFACE,
	/**
	 * 通过线程组件创建的线程
	 */
	TASK,
	/**
	 * 手动创建的线程
	 */
	MANUAL
}
