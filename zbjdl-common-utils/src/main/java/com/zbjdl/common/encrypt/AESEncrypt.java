package com.zbjdl.common.encrypt;

import com.zbjdl.common.utils.StringUtils;

/**
 * Title：AES 加密
 * Description:银行卡信息加密工具类
 * Author: jiawen.huang
 * Date: 14-5-2
 * Time: 15:52
 * Version: 1.0
 * Copyright © 2014 YeePay.com All rights reserved.
 */
public final class AESEncrypt {

	/**
	 * aes密钥
	 */
	public static final String AES_KEY = "dRcwWPwNYEF54aVv";

	/**
	 * 对数据进行aes加密
	 *
	 * @param data
	 * @return
	 */
	public static String aesEncrypt(String data) {
		if (StringUtils.isNotBlank(data)) {
			return AES.encryptToBase64(data, AES_KEY);
		} else {
			throw new RuntimeException("data is null, aesEncrypt fail!");
		}
	}

	/**
	 * 解密aes加密的数据
	 *
	 * @param data
	 * @return
	 */
	public static String aesDecrypt(String data) {
		if (StringUtils.isNotBlank(data)) {
			return AES.decryptFromBase64(data, AES_KEY);
		} else {
			throw new RuntimeException("data is null, aesEncrypt fail!");
		}
	}

}
