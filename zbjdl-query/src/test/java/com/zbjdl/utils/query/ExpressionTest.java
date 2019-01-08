
package com.zbjdl.utils.query;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**    
 */
public class ExpressionTest {

	@Test
	public void test1(){
		String str = "/~a&^%$n\nb~/ /~34i~/";
		
		Pattern p = Pattern.compile("/~([\\s\\S]*?)~/");
		
		Matcher m = p.matcher(str);
		while(m.find()){
			System.out.println(m.group());
		}
	}

}
