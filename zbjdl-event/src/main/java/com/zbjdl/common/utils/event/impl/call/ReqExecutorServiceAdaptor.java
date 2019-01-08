//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.call;

import com.zbjdl.common.utils.event.core.thread.RejectedExecutionHandlerImpl;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReqExecutorServiceAdaptor {
    private static final Logger logger = LoggerFactory.getLogger(ReqExecutorServiceAdaptor.class);
    private static ReqExecutorServiceAdaptor instance;
    private int rrRadio = 20;
    private int rrQueueSize = 1000;
    private long keepAliveTime = 1000L;
    private ExecutorService rrExecutorService;
    private BlockingQueue<Runnable> rrWorkQueue;
    private AtomicBoolean started = new AtomicBoolean(false);

    public static ReqExecutorServiceAdaptor getExecutorServiceAdaptor() {
        return instance;
    }

    public ReqExecutorServiceAdaptor() {
    }

    public void init() {
        instance = this;
        int coreSize = Runtime.getRuntime().availableProcessors();
        int rrMaximumPoolSize = Math.round((float)(coreSize * this.rrRadio) / 10.0F);
        this.rrWorkQueue = new LinkedBlockingQueue(this.rrQueueSize);
        this.rrExecutorService = new ThreadPoolExecutor(coreSize, rrMaximumPoolSize, this.keepAliveTime, TimeUnit.MILLISECONDS, this.rrWorkQueue, new RejectedExecutionHandlerImpl());
        logger.warn("R-R-ThreadPool , coreSize = " + coreSize + " | maxPool = " + rrMaximumPoolSize);
        this.started.compareAndSet(false, true);
    }

    public void destroy() {
        this.started.compareAndSet(true, false);
        this.rrExecutorService.shutdownNow();
    }

    public <T> Future<T> submit(Callable<T> task) {
        return this.rrExecutorService.submit(task);
    }

    public void setRrRadio(int rrRadio) {
        this.rrRadio = rrRadio;
    }

    public void setRrQueueSize(int rrQueueSize) {
        this.rrQueueSize = rrQueueSize;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public AtomicBoolean getStarted() {
        return this.started;
    }
}
