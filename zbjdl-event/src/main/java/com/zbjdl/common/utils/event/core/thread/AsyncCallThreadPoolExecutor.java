//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.thread;

import com.zbjdl.common.utils.event.core.thread.AsyncExecutorServiceAdaptor.NotifyWorker;
import com.zbjdl.common.utils.event.utils.SystemConfigUtils;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncCallThreadPoolExecutor extends ThreadPoolExecutor {
    private static final Logger logger = LoggerFactory.getLogger(AsyncCallThreadPoolExecutor.class);
    private int notifyChance = 1;
    private boolean threadDebug = false;
    private AtomicInteger rrRunning = new AtomicInteger(0);
    private static AtomicInteger rrWaiting = new AtomicInteger(0);
    private AtomicInteger notifyRunning = new AtomicInteger(0);
    private AtomicInteger notifyWaiting = new AtomicInteger(0);

    public AsyncCallThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandlerImpl handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        if (maximumPoolSize > corePoolSize + 5) {
            this.notifyChance = 4;
        } else if (maximumPoolSize > corePoolSize + 4) {
            this.notifyChance = 3;
        } else if (maximumPoolSize > corePoolSize + 3) {
            this.notifyChance = 2;
        }

        this.threadDebug = SystemConfigUtils.getBooleanProperty("thread.pool.debug", false);
    }

    public boolean isBusyForRunning() {
        if (this.threadDebug) {
            logger.warn(this.reportThreadPoolStatus());
        }

        if (this.notifyRunning.get() < this.notifyChance) {
            return false;
        } else {
            return this.notifyWaiting.get() < rrWaiting.get() || this.notifyRunning.get() > this.getCorePoolSize();
        }
    }

    public boolean isBusy() {
        return false;
    }

    public static void downRrWaiting() {
        rrWaiting.decrementAndGet();
    }

    protected void beforeExecute(Thread t, Runnable r) {
        if (r instanceof NotifyWorker) {
            this.notifyRunning.incrementAndGet();
        } else {
            rrWaiting.decrementAndGet();
            this.rrRunning.incrementAndGet();
        }

    }

    protected void afterExecute(Runnable r, Throwable t) {
        if (r instanceof NotifyWorker) {
            this.notifyWaiting.decrementAndGet();
            this.notifyRunning.decrementAndGet();
        } else {
            this.rrRunning.decrementAndGet();
        }

    }

    public void rejectedExecute(Runnable r) {
        if (r instanceof NotifyWorker) {
            this.notifyWaiting.decrementAndGet();
        } else {
            rrWaiting.decrementAndGet();
        }

    }

    public <T> Future<T> submit(Callable<T> task) {
        rrWaiting.incrementAndGet();
        return super.submit(task);
    }

    public void executeTask(Runnable command) {
        this.notifyWaiting.incrementAndGet();

        try {
            super.execute(command);
        } catch (Throwable var3) {
            this.notifyWaiting.decrementAndGet();
        }

    }

    public String reportThreadPoolStatus() {
        StringBuffer buff = new StringBuffer();
        buff.append("C=").append(this.getCorePoolSize());
        buff.append(" M=").append(this.getMaximumPoolSize());
        buff.append(" NC=").append(this.notifyChance);
        buff.append(" RR=").append(this.rrRunning.get());
        buff.append(" RW=").append(rrWaiting.get());
        buff.append(" NR=").append(this.notifyRunning.get());
        buff.append(" NW=").append(this.notifyWaiting.get());
        return buff.toString();
    }
}
