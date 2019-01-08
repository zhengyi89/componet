package com.zbjdl.common.quata;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 配额
 * 
 * @since 2012-8-22
 * @version 1.0
 */
@Target({ ElementType.METHOD })
@Retention(RUNTIME)
public @interface Quota {

	/**
	 * 额度
	 */
	int quota() default 0;

	/**
	 * 超时时间
	 */
	long timeout() default 0;

	/**
	 * <pre>
	 * 使支持接口上定义多个不同系统实现类的资源额度，若与quota()同时配时不起作用
	 * 格式：“系统编号/Quota额度”
	 * 
	 * 例子：
	 * @Quota( {"systemNo1:100", "systemNo2:120"})
	 * public void sampleMethod();
	 * 系统编号为systemNo1的系统该接口实现配额quota=100
	 * 系统编号为systemNo2的系统该接口实现配额quota=120
	 * </pre>
	 */
	String[] quotas() default { "" };

}
