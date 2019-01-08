package com.zbjdl.common.lock;

import com.zbjdl.common.lock.LockJob;

public class SomeJob implements LockJob {

	@Override
	public Object doJob(Object... args) {
		String name = (String)args[0];
		System.out.println(name);
		return name;
	}

}
