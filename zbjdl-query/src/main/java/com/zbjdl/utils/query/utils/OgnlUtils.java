package com.zbjdl.utils.query.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ognl.Ognl;
import ognl.OgnlException;

/**
 * ognl表达式辅助工具
 */
public class OgnlUtils{
	protected static final Log logger = LogFactory.getLog(OgnlUtils.class);
	private static HashMap expressions = new HashMap();
	
	public static Object compile(String expression) throws OgnlException {
        synchronized (expressions) {
            Object o = expressions.get(expression);
            if (o == null) {
                o = Ognl.parseExpression(expression);
                expressions.put(expression, o);
            }
            return o;
        }
    }
	
	public static Object executeExpression(String expression, Object root){
		try {
			return Ognl.getValue(compile(expression), root);
		} catch (OgnlException e) {
			logger.debug(e.getMessage(), e);
			return null;
		}
	}
	
	public static Object executeExpression(String expression, Map context, Object root){
		try {
			return Ognl.getValue(compile(expression), context, root);
		} catch (OgnlException e) {
			logger.debug(e.getMessage(), e);
			return null;
		}
	}
}
