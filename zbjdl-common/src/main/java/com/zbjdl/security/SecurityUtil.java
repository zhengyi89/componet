package com.zbjdl.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

public class SecurityUtil {
	/**
	 * 将二进制转换成16进制
	 * @method parseByte2HexStr
	 * @param buf
	 * @return
	 */
	public static String byte2Hex(byte buf[]){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < buf.length; i++){
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}
	
	/**
	 * 将16进制转换为二进制
	 * @method parseHexStr2Byte
	 * @param hexStr
	 * @return
	 * @throws 
	 * @since v1.0
	 */
	public static byte[] hex2Byte(String hexStr){
		if(hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length()/2];
		for (int i = 0;i< hexStr.length()/2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
	
	 /**
	 * AES加密
	 * @method encrypt
	 * @param content	需要加密的内容
	 * @param password	加密密码
	 * @return
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws UnsupportedEncodingException 
	 * @throws InvalidKeyException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static String encryptAES(String content, String secretKey) {
		byte[] result = new byte[0];
		try {
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");  
			sr.setSeed(secretKey.getBytes());
			
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, sr);
			SecretKey sk = kgen.generateKey();
			byte[] enCodeFormat = sk.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			result = cipher.doFinal(byteContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byte2Hex(result);
	}
	
	/**
	 * AES解密
	 * @method decrypt
	 * @param content	待解密内容
	 * @param password	解密密钥
	 * @return
	 * @throws IOException 
	 */
	public static String decryptAES(String content, String secretKey) {
		try {
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");  
			sr.setSeed(secretKey.getBytes());
			
			byte[] contentByte = hex2Byte(content);
			byte[] result = new byte[0];
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, sr);
			SecretKey sk = kgen.generateKey();
			byte[] enCodeFormat = sk.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = cipher.doFinal(contentByte);
			return new String(result, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 使用md5算法签名
	 * 
	 * @param content
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static String hmacMD5(String content, String secretKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return hmacMD5(content, secretKey, "utf-8");
	}

	/**
	 * 使用md5算法签名
	 * 
	 * @param content
	 * @param key
	 * @param charset
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 */
	public static String hmacMD5(String content, String secretKey, String charset) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String toSign = (content == null ? "" : content) + "&key=" + secretKey;
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] mySign = md5.digest(getContentBytes(toSign, charset));
		
		int i;
        StringBuilder sb = new StringBuilder();
        for (int offset = 0; offset < mySign.length; offset++) {
            i = mySign[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
            	sb.append("0");
            sb.append(Integer.toHexString(i));
        }
        //32位加密
        return sb.toString();
	}
	
	/**
	 * md5加密
	 * @param content
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptMD5(String content){
		byte[] mySign = new byte[0];
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			mySign = md5.digest(getContentBytes(content, "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int i;
        StringBuilder sb = new StringBuilder();
        for (int offset = 0; offset < mySign.length; offset++) {
            i = mySign[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
            	sb.append("0");
            sb.append(Integer.toHexString(i));
        }
        //32位加密
        return sb.toString();
	}
	
	/**
	 * 使用SHA1算法签名
	 * 
	 * @param content
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 * @throws InvalidKeyException 
	 */
	public static String hmacSHA1(String content, String secretKey) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
		return hmacSHA1(content, secretKey, "utf-8");
	}
	
	/**
	 * 使用SHA1算法签名
	 * 
	 * @param content
	 * @param key
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public static String hmacSHA1(String content, String secretKey, String charset) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		byte[] toSign = content.getBytes(charset);
		SecretKey key = new SecretKeySpec(toSign, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(key);
		byte[] mySign = mac.doFinal(toSign);
		
		int i;
        StringBuilder sb = new StringBuilder();
        for (int offset = 0; offset < mySign.length; offset++) {
            i = mySign[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
            	sb.append("0");
            sb.append(Integer.toHexString(i));
        }
        //32位加密
        return sb.toString();
	}
	
	private static byte[] getContentBytes(String content, String charset)
			throws UnsupportedEncodingException {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}

		return content.getBytes(charset);
	}
	
	public static void main(String[] args) {
		System.out.println(encryptMD5("123456"));
	}
}
