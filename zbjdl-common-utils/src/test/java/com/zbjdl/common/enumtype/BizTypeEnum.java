package com.zbjdl.common.enumtype;

import com.zbjdl.common.enumtype.EnumBehavior;

public enum BizTypeEnum implements EnumBehavior {

	/**
	 * 互联网支付
	 */
	INTERNET {
		@Override
		public String getDescription() {
			return "互联网支付";
		}
	},
	/**
	 * 线下收单
	 */
	OFFLINE {
		@Override
		public String getDescription() {
			return "线下收单";
		}
	},
	/**
	 * 代收付
	 */
	PAYMENT {
		@Override
		public String getDescription() {
			return "代收付";
		}
	};

    public static void main(String[] args){
        for (BizTypeEnum stat: BizTypeEnum.values()){
            System.out.println(stat + " desc is " + stat.getDescription());
        }
    }
	
}
