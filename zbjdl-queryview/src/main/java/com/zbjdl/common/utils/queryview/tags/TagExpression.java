/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview.tags;

/**
 * 
 * @since：2012-5-9 下午10:18:40
 * @version:
 */
public class TagExpression {
	private String value;
	private boolean isExpression;

	public static TagExpression parseTagExpression(String expr) {
		if (expr == null) {
			return null;
		}
		TagExpression t = new TagExpression();
		t.isExpression = false;
		if (expr.startsWith("@")) {
			t.value = expr.substring(1, expr.length());
			if (!expr.startsWith("@@")) {
				t.isExpression = true;
			}
		} else {
			t.value = expr;
		}
		return t;
	}

	public String getValue() {
		return value;
	}

	public boolean isExpression() {
		return isExpression;
	}
}
