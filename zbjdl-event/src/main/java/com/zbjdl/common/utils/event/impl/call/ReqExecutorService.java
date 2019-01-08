//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.call;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface ReqExecutorService {
    <T> Future<T> submit(Callable<T> var1);
}
