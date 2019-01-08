
package com.zbjdl.common.enumtype;

/**
 *
 * 定义了自定义枚举需要实现的接口
 * <p>
 * 枚举类型可以声明构造函数（只能是私有或者包级私有）、成员变量和成员方法，也能实现接口。
 * <p>
 * 此接口定义了枚举类需实现的getCode()和getDescription()方法，用以显示一个状态码 和此枚举值的描述。<br/>
 * 适用于需要将枚举转换为一些状态码的情况，例如 ，用于对外接口中的错误码返回 <br/>
 * 下面是一个枚举类的例子。
 * 
 * 例
 * <p><hr><blockquote><pre>
 * 		public enum StatusCodeEnum implements CodeBehavior {
 *	NORMAL("normal","正常"), WARNING("warning","警告"), ERROR("error","错误");
 * 			//描述
 * 			private final String description;
 * 			//代码
 * 			private final String code;
 * 			private StatusCodeEnum(String code,String description) {
 * 				this.code = code;
 * 				this.description = description;
 * 				
 * 			}
 * 			@Override
 * 			public String getDescription() {
 * 				return this.description;
 * 			}
 * 			@Override
 * 			public String getCode() {
 * 				return this.code;
 * 
 * 			}
 * 		
 * 		}
 * 
 * </pre></blockquote><hr>
 * <p>
 * 
 *  
 * 
 * 
 * @author yejiyong
 *
 */
public interface CodeBehavior extends EnumBehavior{

	
	/**
	 * 取得枚举的code值
	 * @return
	 */
	public String getCode();
	

	
	
	

}
