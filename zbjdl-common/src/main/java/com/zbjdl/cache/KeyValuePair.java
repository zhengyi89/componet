package com.zbjdl.cache;

import java.io.Serializable;

/**
 * 键值类型对象
 * 
 *
 * @param <K>
 * @param <V>
 */
public class KeyValuePair<K,V> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public KeyValuePair(){
		
	}
	
	public KeyValuePair(K k, V v){
		this.key=k;
		this.value=v;
	}
	
	/**
	 * @return 键
	 */
	private K key;
	
	/**
	 * @return 值
	 */
	private V value;
	
	public K getKey() {
		return key;
	}
	public KeyValuePair<K,V> setKey(K key) {
		this.key = key;
		return this;
	}
	public V getValue() {
		return value;
	}
	public KeyValuePair<K,V> setValue(V value) {
		this.value = value;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		if(!(obj instanceof KeyValuePair))
			return false;
		if(this==obj)
			return true;
		
		@SuppressWarnings("unchecked")
		final KeyValuePair<K,V> kv = (KeyValuePair<K,V>)obj;
		return this.getKey().equals(kv.getKey())
			&& this.getValue().equals(kv.getValue());
	}
	
	@Override
	public int hashCode() {
		return this.getKey().hashCode() ^ this.getValue().hashCode();
	}
}
