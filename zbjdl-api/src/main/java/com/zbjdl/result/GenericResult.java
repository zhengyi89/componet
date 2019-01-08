package com.zbjdl.result;

/***
 * 有单个返回值结果
 * 
 *
 * @param <T>
 */
public class GenericResult<T> extends Result {

	private static final long serialVersionUID = 1L;
	
	public GenericResult(){
		this.setCode(Result.SUCCESS);
	}
	
	public static <E> GenericResult<E> newGenericResult() {
		return new GenericResult<E>();
	}
	
	/**
	 * @return 单个返回值
	 */
	private T value;

	public T getValue() {
		return value;
	}

	public GenericResult<T> setValue(T value) {
		this.value = value;
		return this;
	}

	@Override
	public boolean isError() {
		return !Result.SUCCESS.equals(this.getCode());
	}
}
