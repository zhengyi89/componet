package test.common;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.Test;

import com.zbjdl.security.SecurityUtil;

public class SecurityTest {
	 @Test
	public void test2(){
		String s = "E9ED102208FE2FC998EC379ED1F28F84485B708140177D654B1F1775FCF7C674EA2AF0AE65623005A64CA9DD36C76541EC238D0B990C9955DB2BBE2AFA938F2DEE58539067EEA2A9DE3C58CBC3812B7796418C158FED637B15EF968E8910FB6AD102FB0BE00C806CDB654FF53043974548E1E81AEE85213069416F22FC07BE313E2BF4A94952E0658914E05C2CBA657EF7FF392C4FEB6218469D17247943794A31986F82A6E1FE23D0972CE394EB5F3D61C4E74748CF18BCC5B6E8E2F9580ADE089ADCE55644593995B1F62295068B72D38D055B275477FBE8FD662C8961F751";
		String data = SecurityUtil.decryptAES(s, "123456789");
		System.out.println(data);
	}
	
    @Test
    public void test() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        String s = "{\"lastUpdated\":1431928266454,\"loginType\":\"site\",\"logonName\":\"zhengbaoda\",\"password\":\"123456\",\"userInfo\":{\"customerGategory\":70,\"merchantCode\":\"4000101\",\"platformCode\":\"4000101\",\"userCode\":\"4000101\",\"userId\":\"1101003\"}}";
        String p = new String("abcde");
        
        System.out.println("原始：" + s);
        String encryptResultStr = SecurityUtil.encryptAES(s,p);

		System.out.println("encryptAES加密后：" + encryptResultStr);
//		byte[] decryptFrom = SecurityUtil.hex2Byte(encryptResultStr);
		String deData = SecurityUtil.decryptAES(encryptResultStr,p);//解码
		System.out.println("decryptAES解密后：" + deData);
		
		// 应用密钥
		String appSecret = "a5f8fe5k59eb0c6534787b6d1a739192";
		
		String signContent = "aName=lihong2&bName=lihong1&cName=lihong5&dName=lihong4&fName=lihong3&gName=lihong0";
		System.out.println("sign content is:" + signContent);
		
		// 调用SDK方法，获得参数签名值
		String strSignValue = SecurityUtil.hmacMD5(signContent,appSecret);
		System.out.println("String md5 sign result is : "+strSignValue);
		
		// 调用SDK方法，获得参数签名值
		String strSignValue2 = SecurityUtil.hmacSHA1(signContent, appSecret);
		System.out.println("String sha1 sign result is : "+strSignValue2);
    }
}
