
package com.zbjdl.utils.query.jdbc;

import java.util.HashMap;


public class JdbcResultMap extends HashMap{
	
	public Object get(Object key) {
		key = key.toString().toLowerCase();
		return super.get(key);
		
	}
	
	public Object put(Object key, Object value) {
		key = key.toString().toLowerCase();
		return super.put(key, value);
	}
}
