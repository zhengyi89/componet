/** 
 * Copyright: Copyright (c)2015
 * Company: 云宝金服
 */
package com.zbjdl.common.amount;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 百分比对象
 * 
 */
@SuppressWarnings("serial")
public class Percent implements Serializable{

	/**
	 * 百分比值
	 */
	private BigDecimal value;
	
	 /**
     * 无参构造器
     */
	public Percent() {
		super();
	}
	
	public Percent(BigDecimal value) {
		if (value == null) {
			throw new RuntimeException("无效的百分比值[value=" + value + "]");
		}
		// 保留小数点后6位有效数字
		this.value = value.setScale(6, BigDecimal.ROUND_HALF_UP);
	}

	public Percent(String value) {
		this(new BigDecimal(value));
	}

	public Percent(float value) {
		this(new BigDecimal(value));
	}

	public Percent(double value) {
		this(new BigDecimal(value));
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
