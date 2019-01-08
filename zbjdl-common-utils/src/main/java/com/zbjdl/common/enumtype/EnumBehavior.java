package com.zbjdl.common.enumtype;
/**
 * 
 * 定义了自定义枚举需要实现的接口
 * <p>
 * 枚举类型可以声明构造函数（只能是私有或者包级私有）、成员变量和成员方法，也能实现接口。</br>
 * 此接口定义了枚举类需实现的getDescription()方法，用以显示枚举值的描述。<br/>
 * 见下面的例子，主要用以给枚举显示一个中文名称</br>
 * </p>
 * 例:
 * <p><hr><blockquote><pre>
 * public enum Status implements EnumBehavior {
 *	NORMAL("正常"), WARNING("警告"), ERROR("错误");
 *	//描述
 *	private final String description;
 *	private Status(String description) {
 *		this.description = description;
 *	}
 *	@Override
 *	public String getDescription() {
 *		return this.description;
 *	}
 *
 *}
 * </pre></blockquote><hr>
 * <p>
 * 
 * 
 * 
 * @author yejiyong
 *
 */
public interface EnumBehavior {
	/**
	 * 获得枚举的描述
	 * @return
	 */
    public String getDescription();


}
