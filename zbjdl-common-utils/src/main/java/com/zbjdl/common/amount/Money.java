/** 
 * Copyright: Copyright (c)2015
 * Company: 云宝金服
 */
package com.zbjdl.common.amount;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 货币对象，含金额和币种
 * 
 * @version:
 */
@SuppressWarnings("serial")
public final class Money implements Serializable {

	/**
	 * 金额
	 */
	private Amount amount;

	/**
	 * 币种
	 */
	private CurrencyEnum currency;

	public Money() {
		super();
	}

	public Money(Amount amount) {
		super();
		this.amount = amount;
		this.currency = CurrencyEnum.CNY;
	}

	public Money(Amount amount, CurrencyEnum currency) {
		super();
		this.amount = amount;
		this.currency = currency;
	}

	public Money(BigDecimal amount) {
		super();
		this.amount = new Amount(amount);
		this.currency = CurrencyEnum.CNY;
	}

	public Money(BigDecimal amount, CurrencyEnum currency) {
		super();
		this.amount = new Amount(amount);
		this.currency = currency;
	}

	public Money(String amount) {
		super();
		this.amount = new Amount(amount);
		this.currency = CurrencyEnum.CNY;
	}

	public Money(String amount, CurrencyEnum currency) {
		super();
		this.amount = new Amount(amount);
		this.currency = currency;
	}

	/**
	 * 金额是否大于0
	 * 
	 * @return
	 */
	public boolean isPositive() {
		return this.amount.isPositive();
	}

	/**
	 * 金额是否等于0
	 * 
	 * @return
	 */
	public boolean isZero() {
		return this.amount.isZero();
	}

	/**
	 * 金额是否小于0
	 * 
	 * @return
	 */
	public boolean isNegative() {
		return this.amount.isNegative();
	}

	/**
	 * 金额相加
	 * 
	 * @param amount
	 *            被加金额
	 * @return
	 */
	public final Money add(Money money) {
		if (this.currency != money.getCurrency()) {
			throw new RuntimeException("币种不一致[currency1="
					+ this.currency.name() + ";currency2="
					+ money.getCurrency().name() + "]");
		}
		return new Money(this.getAmount().add(money.getAmount()), this.currency);
	}

	/**
	 * 金额相减
	 * 
	 * @param amount
	 *            被减金额
	 * @return
	 */
	public final Money subtract(Money money) {
		if (this.currency != money.getCurrency()) {
			throw new RuntimeException("币种不一致[currency1="
					+ this.currency.name() + ";currency2="
					+ money.getCurrency().name() + "]");
		}
		return new Money(this.getAmount().subtract(money.getAmount()),
				this.currency);
	}

	/**
	 * 金额乘以比例
	 * 
	 * @param percent
	 *            比例
	 * @return
	 */
	public final Money multiply(Percent percent) {
		return new Money(this.getAmount().multiply(percent), this.getCurrency());
	}

	public Amount getAmount() {
		return amount;
	}

	public CurrencyEnum getCurrency() {
		return currency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode())
				+ ((currency == null) ? 0 : currency.hashCode());
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
		Money other = (Money) obj;
		return this.amount.equals(other.amount)
				&& this.currency.equals(other.currency);
	}

}
