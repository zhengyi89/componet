/** 
 * Copyright: Copyright (c)2015
 * Company: 云宝金服
 */
package com.zbjdl.common.amount;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 金额对象
 * 
 * @since：2011-3-9 下午05:51:28
 * @version:
 */
public class Amount implements Serializable {

	private static final long serialVersionUID = -4099120713042150972L;

	/**
	 * 金额值，默认单位为元
	 */
	private BigDecimal value;

	/**
	 * 精度
	 */
	private int accuracy;

	/**
	 * 默认精度
	 */
	private static final int DEFAULT_ACCURACY = 2;

	/**
	 * 允许的最小值
	 */
	private static final BigDecimal MIN_VALUE = new BigDecimal(-1000000000000L);

	/**
	 * 允许的最大值
	 */
	private static final BigDecimal MAX_VALUE = new BigDecimal(1000000000000L);

	/**
	 * 无参构造器
	 */
	public Amount() {
		this(BigDecimal.ZERO, DEFAULT_ACCURACY);
	}

	/**
	 * 不带精度的构造器，默认两位精度
	 * 
	 * @param value
	 */
	public Amount(BigDecimal value) {
		this(value, DEFAULT_ACCURACY);
	}

	/**
	 * 不带精度的构造器，默认两位精度
	 * 
	 * @param value
	 */
	public Amount(long value) {
		this(new BigDecimal(value), DEFAULT_ACCURACY);
	}

	/**
	 * 带精度的构造器
	 * 
	 * @param value
	 * @param accuracy
	 */
	public Amount(long value, int accuracy) {
		this(new BigDecimal(value), accuracy);
	}

	/**
	 * 不带精度的构造器，默认两位精度
	 * 
	 * @param value
	 */
	public Amount(double value) {
		this(new BigDecimal(value), DEFAULT_ACCURACY);
	}

	/**
	 * 带精度的构造器
	 * 
	 * @param value
	 * @param accuracy
	 */
	public Amount(double value, int accuracy) {
		this(new BigDecimal(value), accuracy);
	}

	/**
	 * 不带精度的构造器，默认两位精度
	 * 
	 * @param value
	 */
	public Amount(int value) {
		this(new BigDecimal(value), DEFAULT_ACCURACY);
	}

	/**
	 * 带精度的构造器
	 * 
	 * @param value
	 * @param accuracy
	 */
	public Amount(int value, int accuracy) {
		this(new BigDecimal(value), accuracy);
	}

	/**
	 * 不带精度的构造器，默认两位精度
	 * 
	 * @param value
	 */
	public Amount(String value) {
		this(new BigDecimal(value), DEFAULT_ACCURACY);
	}

	/**
	 * 带精度的构造器
	 * 
	 * @param value
	 * @param accuracy
	 */
	public Amount(String value, int accuracy) {
		this(new BigDecimal(value), accuracy);
	}

	/**
	 * 带精度的构造器
	 * 
	 * @param value
	 * @param accuracy
	 */
	public Amount(BigDecimal value, int accuracy) {
		// 检查金额数值范围是否合法
		if (value == null || value.compareTo(MIN_VALUE) == -1
				|| value.compareTo(MAX_VALUE) == 1) {
			throw new RuntimeException("无效的金额数值[value=" + value + "]");
		}
		// 将金额数值从第N+1位四舍五入，保留N位小数
		this.accuracy = accuracy;
		this.value = value.setScale(this.accuracy, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 判断本金额是否大于某金额
	 * 
	 * @param amount
	 * @return
	 */
	public final boolean isGreaterThan(Amount amount) {
		return value.compareTo(amount.getValue()) > 0;
	}

	/**
	 * 判断本金额是否等于某金额
	 * 
	 * @param amount
	 * @return
	 */
	@Deprecated
	public final boolean isEqualTo(Amount amount) {
		return value.compareTo(amount.getValue()) == 0;
	}

	/**
	 * 判断本金额是否小于某金额
	 * 
	 * @param amount
	 * @return
	 */
	public final boolean isLesserThan(Amount amount) {
		return value.compareTo(amount.getValue()) < 0;
	}

	/**
	 * 判断本金额是否为正数
	 * 
	 * @return
	 */
	public final boolean isPositive() {
		return value.compareTo(BigDecimal.ZERO) > 0;
	}

	/**
	 * 判断本金额是否为0
	 * 
	 * @return
	 */
	public final boolean isZero() {
		return value.compareTo(BigDecimal.ZERO) == 0;
	}

	/**
	 * 判断本金额是否为负数
	 * 
	 * @return
	 */
	public final boolean isNegative() {
		return value.compareTo(BigDecimal.ZERO) < 0;
	}

	/**
	 * 金额相加
	 * 
	 * @param amount
	 *            被加金额
	 * @return
	 */
	public final Amount add(Amount amount) {
		return new Amount(this.value.add(amount.getValue()));
	}

	/**
	 * 金额相减
	 * 
	 * @param amount
	 *            被减金额
	 * @return
	 */
	public final Amount subtract(Amount amount) {
		return new Amount(this.value.subtract(amount.getValue()));
	}

	/**
	 * 金额乘以比例
	 * 
	 * @param percent
	 *            比例
	 * @return
	 */
	public final Amount multiply(Percent percent) {
		return new Amount(this.value.multiply(percent.getValue()));
	}

	/**
	 * 金额乘以倍数
	 * 
	 * @param multiple
	 *            倍数
	 * @return
	 */
	public final Amount multiply(long multiple) {
		return new Amount(this.value.multiply(new BigDecimal(multiple)));
	}

	/**
	 * 金额乘以倍数
	 * 
	 * @param multiple
	 *            倍数
	 * @return
	 */
	public final Amount multiply(int multiple) {
		return new Amount(this.value.multiply(new BigDecimal(multiple)));
	}

	/**
	 * 金额乘以倍数
	 * 
	 * @param multiple
	 *            倍数
	 * @return
	 */
	public final Amount multiply(double multiple) {
		return new Amount(this.value.multiply(new BigDecimal(multiple)));
	}

	/**
	 * 获取金额值
	 * 
	 * @return
	 */
	public BigDecimal getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Amount other = (Amount) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return value != null ? value.toString() : null;
	}
}
