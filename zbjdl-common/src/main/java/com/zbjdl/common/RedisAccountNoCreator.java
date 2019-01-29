package com.zbjdl.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.zbjdl.utils.ZbjdlUtil;


public class RedisAccountNoCreator {
	
	private final String NUMBER_KEY = "chanpay_acount_no";
	private final String NUMBER_FORMAT="000000";
	
	@Autowired
	private RedisTemplate<String, Long> redisTemplate;
	
	public String createAccountNo(String orgCode, String subCode, Integer accountType){
		Long number = redisTemplate.opsForValue().increment(NUMBER_KEY, 1l);
		StringBuffer sb = new StringBuffer();
		sb.append((ZbjdlUtil.fixString(orgCode, 3, '0')))
			.append(ZbjdlUtil.fixString(subCode, 6, '0'))
			.append(ZbjdlUtil.formatDecimal(accountType, "00"))
			.append(ZbjdlUtil.formatDecimal(number, NUMBER_FORMAT));
		
		return sb.toString();
	}
}
