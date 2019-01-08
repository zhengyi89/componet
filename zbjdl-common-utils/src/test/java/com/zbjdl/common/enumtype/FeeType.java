package com.zbjdl.common.enumtype;

import com.zbjdl.common.enumtype.IndexBehavior;

/**
 * 计费类型
 * @author yejiyong
 *
 */
public enum FeeType implements IndexBehavior {

	/**
	 * 基础利率
	 */
	LOAN_FEE(1, "基础利率"),
	
	/**
	 * 平台推广利率计费类型
	 */
	PROMO_FEE(2, "平台推广利率"),
	
	/**
	 * 账户管理费计费类型
	 */
	MANAGE_FEE(3, "账户管理费"),
	
	/**
	 * 风险管理费
	 */
	RISK_FEE(4, "风险管理费"),
	
	/**
	 * 咨询服务费
	 */
	CONSULT_FEE(5, "咨询服务费"),
	
	/**
	 * 担保费
	 */
	VOUCH_FEE(6, "担保费"),
	
	/**
	 * 滞纳金
	 */
	LATE_FEE(7, "滞纳金");
	
	
	private final int index;
	
	private final String description;
	
	private FeeType(int index, String description) {
		
		this.index = index;
		this.description = description;
	}

	@Override
	public Integer getIndex() {

		return this.index;
	}

    public static  String getName(int index) {  
        for (FeeType type : FeeType.values()) {  
            if (type.getIndex() == index) {  
                return type.name();  
            }  
        }  
        return null;  
    }

	@Override
	public String getDescription() {
		return this.description;
	}  
	
}
