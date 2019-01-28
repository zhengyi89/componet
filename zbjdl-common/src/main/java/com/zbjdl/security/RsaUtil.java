/**
 * 功能描述：rsa加密算法，主要用来做与支付宝的签名
 */

package com.zbjdl.security;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

/**
 * 经典的数字签名算法RSA 数字签名
 * 
 * */
public class RsaUtil {
	
	public static final String KEY_ALGORTHM="RSA";//
	public static final String SIGNATURE_ALGORITHM="SHA1WithRSA";
	 
	public static final String PUBLIC_KEY = "RSAPublicKey";//公钥
	public static final String PRIVATE_KEY = "RSAPrivateKey";//私钥
	
	/**
     * 初始化密钥
     * @return
     * @throws Exception
     */
    public static Map<String,Object> initKey()throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORTHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
         
        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //私钥
        RSAPrivateKey privateKey =  (RSAPrivateKey) keyPair.getPrivate();
         
        Map<String,Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
         
        return keyMap;
    }
    
    /**
     * 初始化密钥
     * @return
     * @throws Exception
     */
    public static Map<String,Object> initKey(String algorithm)throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORTHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
         
        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //私钥
        RSAPrivateKey privateKey =  (RSAPrivateKey) keyPair.getPrivate();
         
        Map<String,Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
         
        return keyMap;
    }
    
    /**
     * 取得公钥，并转化为String类型
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)throws Exception{
        Key key = (Key) keyMap.get(PUBLIC_KEY);  
        return Base64.encodeBase64String(key.getEncoded());     
    }
 
    /**
     * 取得私钥，并转化为String类型
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception{
        Key key = (Key) keyMap.get(PRIVATE_KEY);  
        return Base64.encodeBase64String(key.getEncoded());     
    }
    
    /**
     * 用私钥加密
     * @param data  加密数据
     * @param key   密钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data,String key)throws Exception{
        //解密密钥
        byte[] keyBytes = Base64.decodeBase64(key);
        //取私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
         
        //对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
         
        return cipher.doFinal(data);
    }
    
    /**
     * 用私钥解密
     * @param data  加密数据
     * @param key   密钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data,String key)throws Exception{
        //对私钥解密
        byte[] keyBytes = Base64.decodeBase64(key);
         
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
         
        return cipher.doFinal(data);
    }
    
    /**
     * 用公钥加密
     * @param data  加密数据
     * @param key   密钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data,String key)throws Exception{
        //对公钥解密
        byte[] keyBytes = Base64.decodeBase64(key);
        //取公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        
        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
         
        return cipher.doFinal(data);
    }
    
    /**
     * 用公钥解密
     * @param data  加密数据
     * @param key   密钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data,String key)throws Exception{
        //对私钥解密
        byte[] keyBytes = Base64.decodeBase64(key);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
         
        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
         
        return cipher.doFinal(data);
    }
    
    /**
     *  用私钥对信息生成数字签名
     * @param data  //加密数据
     * @param privateKey    //私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data,String privateKey)throws Exception{
        //解密私钥
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        //构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取私钥匙对象
        PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey2);
        signature.update(data);
         
        return Base64.encodeBase64String(signature.sign());
    }
    
    /**
     * 校验数字签名
     * @param data  加密数据
     * @param publicKey 公钥
     * @param sign  数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data,String publicKey,String sign)throws Exception{
        //解密公钥
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        //构造X509EncodedKeySpec对象
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取公钥匙对象
        PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);
         
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey2);
        signature.update(data);
        //验证签名是否正常
        return signature.verify(Base64.decodeBase64(sign));
    }
    
//    public static void main(String[] args) throws Exception {
//		Map<String,Object> keyPair = RsaUtil.initKey();
//		String publicKey = getPublicKey(keyPair);
//		String privateKey = getPrivateKey(keyPair);
//		System.out.println(publicKey+"\n" + privateKey.length());
//		
////		私钥加密，公钥解密
//		byte[] pEncryptData = RsaUtil.encryptByPrivateKey("fdskaslddfjkldsalkadsj 你好，123哈哈".getBytes(),privateKey);
//		
//		System.out.println(Hex.encodeHexString(pEncryptData));
//		String pEncryptStr = Base64.encodeBase64String(pEncryptData);
//		System.out.println("private:" + pEncryptStr);
//		byte[] pEncryptData2 = Base64.decodeBase64(pEncryptStr);
//		
//		byte[] pDecryptData = RsaUtil.decryptByPublicKey(pEncryptData2, publicKey);
//		String pDecrptStr = new String(pDecryptData);
//		System.out.println("private:" + pDecrptStr);
//		
//		//公钥加密，私钥解密
//		byte[] kEncryptData = RsaUtil.encryptByPublicKey("fdskaslddfjkldsalkadsj 你好，123哈哈".getBytes(),publicKey);
//		System.out.println(Hex.encodeHexString(kEncryptData));
//		String kEncryptStr = Base64.encodeBase64String(kEncryptData);
//		System.out.println("public:" + kEncryptStr);
//		byte[] kEncryptData2 = Base64.decodeBase64(kEncryptStr);
//		
//		byte[] kDecryptData = RsaUtil.decryptByPrivateKey(kEncryptData2, privateKey);
//		String kDecrptStr = new String(kDecryptData);
//		System.out.println("public:" + kDecrptStr);
//		
//		//加验签名
//		String sign = RsaUtil.sign("123你好afdsaasdsaddsfadsffsdasd".getBytes(), privateKey);
//		System.out.println(sign);
//		Boolean result = RsaUtil.verify("123你好afdsaasdsaddsfadsffsdasd".getBytes(), publicKey, sign);
//		System.out.println(result);
//	}
	
	public static boolean verifyData(String requestXml, String sign,String publicKeyString) {
		try {
			return RSACoder.verify(requestXml.getBytes(), publicKeyString, sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String signData(String xml,String privateKeyString) {
		try {
			return RSACoder.sign(xml.getBytes(), privateKeyString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String encode(String xml,String privateKeyString){
		try {
//			return Base64Util.ENCODER.encode(RSACoder.encryptByPrivateKey(xml.getBytes(), privateKeyString));
			return Base64.encodeBase64String(RSACoder.encryptByPrivateKey(xml.getBytes(), privateKeyString));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decode(String xml,String publicKeyString){
		try {
//			return Base64Util.ENCODER.encode(RSACoder.decryptByPublicKey(xml.getBytes(), publicKeyString));
			return Base64.encodeBase64String(RSACoder.decryptByPublicKey(xml.getBytes(), publicKeyString));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
}
