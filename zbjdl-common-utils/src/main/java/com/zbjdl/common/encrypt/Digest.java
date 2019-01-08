/** 
 * Copyright: Copyright (c)2015
 * Company: 云宝金服
 */
package com.zbjdl.common.encrypt;

import java.security.MessageDigest;
/**   
 * 计算摘要的工具类 
 */
public class Digest {
    
    /**
     * 使用MD5算法计算摘要，并对结果进行hex转换
     * @param data 源数据
     * @return 摘要信息
     */
    public static String md5Digest(String str){
    	try {
    		byte[] data = str.getBytes(ConfigureEncryptAndDecrypt.CHAR_ENCODING);
			MessageDigest md = MessageDigest.getInstance(ConfigureEncryptAndDecrypt.MD5_ALGORITHM);
			return Hex.toHex(md.digest(data));
		} catch (Exception e){
			throw new RuntimeException("digest fail!", e);
		}
    }
    
    /**
     * 使用SHA-0算法计算摘要，并对结果进行hex转换
     * @param str 源数据
     * @return 摘要信息
     */
    public static String shaDigest(String str){
    	try {
    		byte[] data = str.getBytes(ConfigureEncryptAndDecrypt.CHAR_ENCODING);
			MessageDigest md = MessageDigest.getInstance(ConfigureEncryptAndDecrypt.SHA_ALGORITHM);
			return Hex.toHex(md.digest(data));
		} catch (Exception e){
			throw new RuntimeException("digest fail!", e);
		}
    }
    
    /**
     * 根据指定算法计算摘要
     * @param str 源数据
     * @param alg 摘要算法
     * @param charencoding 源数据获取字节的编码方式
     * @return 摘要信息
     */
    public static String digest(String str, String alg, String charencoding){
    	try {
    		byte[] data = str.getBytes(charencoding);
			MessageDigest md = MessageDigest.getInstance(alg);
			return Hex.toHex(md.digest(data));
		} catch (Exception e){
			throw new RuntimeException("digest fail!", e);
		}
    }
}
