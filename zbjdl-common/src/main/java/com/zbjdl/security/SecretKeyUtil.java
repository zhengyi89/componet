package com.zbjdl.security;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecretKeyUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(SecretKeyUtil.class);
	
	public static void main(String[] args) {
		System.out.println(generateSecretKey());
	}
	
	private static String byte2HexStr(byte[] secretKey){
		int i;
        StringBuilder sb = new StringBuilder();
        for (int offset = 0; offset < secretKey.length; offset++) {
            i = secretKey[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
            	sb.append("0");
            sb.append(Integer.toHexString(i));
        }
        
        return sb.toString();
	}

	/**
	 * 生成随机密钥
	 * @return
	 */
	public static String generateSecretKey(){
		byte[] secretKey = new byte[0];

		try {
			//得到一个 指定算法密钥的密钥生成器
			KeyGenerator keygen = KeyGenerator.getInstance("HmacSHA1");
			keygen.init(128);
			Key key = keygen.generateKey();

			secretKey = key.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			logger.error("NoSuchAlgorithmException:{}", e);
		}

		return byte2HexStr(secretKey);
	}
}
