package com.zbjdl.common.mvel;

import java.util.Map;

import org.mvel2.MVEL;
import org.mvel2.templates.TemplateRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**MVEL表达式工具类
 */
public class MVELUtils {
	private static final Logger logger = LoggerFactory.getLogger(MVELUtils.class);

	/**
	 * 执行一个mvel表达式
	 * @param expression 原始的表达式字符串
	 * @param ctx 根对象
	 * @param vars 参数集
	 * @return
	 */
	public static Object eval(String expression, Object ctx, Map<String, Object> vars){
		try{
			return MVEL.eval(expression, ctx, vars);
		}catch(Exception e){
			logger.debug("eval expression fail : "+e.getMessage());
			return null;
		}
	}
	
	/**
	 * 执行一个mvel表达式
	 * @param <T>
	 * @param expression 原始的表达式字符串
	 * @param ctx 根对象
	 * @param vars 参数集
	 * @param toType 转换目标类型
	 * @return
	 */
	public static<T> T eval(String expression, Object ctx, Map<String, Object> vars, Class<T> toType){
		try{
			return (T)MVEL.eval(expression, ctx, vars, toType);
		}catch(Exception e){
			logger.debug("eval expression fail : "+e.getMessage());
			return null;
		}
	}
	
	/**
	 * 执行一个MVEL模板文件
	 * @param template 模板文件内容
	 * @param ctx 根对象
	 * @param vars 参数集
	 * @return
	 */
	public static String evalTemplate(String template, Object ctx,  Map<String, Object> vars){
		try{
			Object result = TemplateRuntime.eval(template, ctx, vars);
			return result!=null?result.toString():null;
		}catch(Exception e){
			logger.debug("eval expression fail : "+e.getMessage());
			return null;
		}
	}
	
}
