package com.zbjdl.common.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 日志字段 注解
 *
 */

@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface LogColumn {

	/** 字段名称 */
	String name();

	/**
	 * 最大长度，超长默认按字节截取
	 */
	int length() default -1;

	/**
	 * 超长后截取类型
	 */
	SubType subtype() default SubType.BYTE;

	public enum SubType {
		BYTE, CHAR
	}
}
