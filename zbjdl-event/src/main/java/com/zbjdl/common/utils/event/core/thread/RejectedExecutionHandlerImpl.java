//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {
    private AsyncCallThreadPoolExecutor executorService;

    public RejectedExecutionHandlerImpl() {
    }

    public void setExecutor(AsyncCallThreadPoolExecutor executor) {
        this.executorService = executor;
    }

    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        this.executorService.rejectedExecute(r);
    }
}
