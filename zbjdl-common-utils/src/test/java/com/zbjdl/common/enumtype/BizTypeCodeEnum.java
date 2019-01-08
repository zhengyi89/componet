package com.zbjdl.common.enumtype;

import com.zbjdl.common.enumtype.CodeBehavior;

public enum BizTypeCodeEnum implements CodeBehavior {

	/**
	 * 互联网支付
	 */
	INTERNET {
		@Override
		public String getDescription() {
			return "互联网支付";
		}

		@Override
		public String getCode() {
			return "INTERNET";
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

		@Override
		public String getCode() {
			return "OFFLINE";
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

		@Override
		public String getCode() {
			return "PAYMENT";
		}
	};

    public static void main(String[] args){
        for (BizTypeCodeEnum stat: BizTypeCodeEnum.values()){
            System.out.println(stat + " desc is " + stat.getDescription());
        }
    }
	
}
