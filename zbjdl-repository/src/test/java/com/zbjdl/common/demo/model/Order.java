package com.zbjdl.common.demo.model;

import java.util.List;

import com.zbjdl.common.amount.Amount;
import com.zbjdl.common.respository.entity.VersionableEntity;

public class Order extends VersionableEntity{
	/**
	 * 订单号
	 */
	private String orderNumber;
	/**
	 * 商品名称
	 */
	private String productName;

	/**
	 * 金额
	 */
	
	private Amount amount;
	
	private List<String> itemNames;

	/**
	 * 状态
	 */
	private String status;
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getStatus() {
		return status;
	}
	public Amount getAmount() {
		return amount;
	}
	public void setAmount(Amount amount) {
		this.amount = amount;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<String> getItemNames() {
		return itemNames;
	}
	public void setItemNames(List<String> itemNames) {
		this.itemNames = itemNames;
	}
	
	
}
