
package com.zbjdl.common.enumtype;


/**
 *
 * 定义了自定义枚举需要实现的接口
 * <p>
 *  枚举类型可以声明构造函数（只能是私有或者包级私有）、成员变量和成员方法，也能实现接口。
 * <p>
 * 此接口定义了枚举类需实现的getIndex()和getDescription()方法，用以显示一个顺序索引 和此枚举值的描述。<br/>

 * 下面是一个枚举类的例子。
 *  
 * 例
 * <p><hr><blockquote><pre>
 * 	public enum StatusIndexEnum implements IndexBehavior {
 * 	
 *	NORMAL(1, "正常"), WARNING(2, "警告"), ERROR(3, "错误");
 * 		// 描述
 * 		private final String description;
 * 		// 索引
 * 		private final Integer index;
 * 	
 * 		private StatusIndexEnum(Integer index, String description) {
 * 			this.index = index;
 * 			this.description = description;
 * 	
 * 		}
 * 	
 * 		@Override
 * 		public String getDescription() {
 * 			return this.description;
 * 		}
 * 	
 * 		@Override
 * 		public Integer getIndex() {
 * 			return this.index;
 * 		}
 * 	
 * 	    public static  String name(int index) {  
 * 	        for (StatusIndexEnum status : StatusIndexEnum.values()) {  
 * 	            if (status.getIndex() == index) {  
 * 	                return status.name();  
 * 	            }  
 * 	        }  
 * 	        return null;  
 * 	    }
 * 	}
 * 
 * </pre></blockquote><hr>
 * <p>
 *  
 *   
 *  
 *  @author yejiyong
 * 
 */
public interface IndexBehavior extends EnumBehavior{

	
	/**
	 * 取得枚举的索引
	 * @return
	 */
	public Integer getIndex();
	

}
