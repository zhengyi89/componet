package com.zbjdl.common.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 日志表名      注解
 */

@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface LogTable {
	
	/**
	 * 表名
	 * @return
	 */
	String tableName();
}
