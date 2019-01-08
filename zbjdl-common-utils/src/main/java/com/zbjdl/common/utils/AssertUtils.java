package com.zbjdl.common.utils;


/**
 * 断言工具
 * @author: mian.liu    
 * @since: 2012-10-10 下午2:07:08 
 * @version:
 */
public class AssertUtils {

	/**
	 * 表达式是否为真
	 * @param expression 表达式
	 * @param message 断言失败时的消息
	 * @throws IllegalArgumentException
	 */
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 表达式是否为真
	 * @param expression 表达式
	 * @throws IllegalArgumentException
	 */
	public static void isTrue(boolean expression) {
		isTrue(expression, "[断言失败] - 表达式必须为真");
	}

	/**
	 * 对象是null
	 * @param object 对象
	 * @param message 断言失败时的消息
	 * @throws IllegalArgumentException
	 */
	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 对象是null
	 * @param object 对象
	 * @throws IllegalArgumentException
	 */
	public static void isNull(Object object) {
		isNull(object, "[断言失败] - 对象必须为null");
	}

	/**
	 * 对象不是null
	 * @param 对象
	 * @param 失败时的消息
	 * @throws IllegalArgumentException
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 对象不是null
	 * @param object
	 * @throws IllegalArgumentException
	 */
	public static void notNull(Object object) {
		notNull(object, "[断言失败] - 对象不能为空");
	}


}
