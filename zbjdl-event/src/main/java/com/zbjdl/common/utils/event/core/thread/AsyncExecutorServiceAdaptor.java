//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.thread;

import com.zbjdl.common.utils.ThreadContextType;
import com.zbjdl.common.utils.ThreadContextUtils;
import com.zbjdl.common.utils.event.impl.call.ReqExecutorService;
import com.zbjdl.common.utils.event.impl.notify.AsyncReliableSokulaEvent;
import com.zbjdl.common.utils.event.impl.notify.NotifyExecutorService;
import com.zbjdl.common.utils.event.impl.notify.NotifyQueue;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncExecutorServiceAdaptor implements NotifyExecutorService, ReqExecutorService {
    private static final Logger logger = LoggerFactory.getLogger(AsyncExecutorServiceAdaptor.class);
    private int radio = 50;
    private int notifyQueueSize;
    private long maxThreadQueueSize;
    private long keepAliveTime = 1000L;
    private AsyncCallThreadPoolExecutor executorService;
    private NotifyQueue notifyQueue;
    private AtomicBoolean started = new AtomicBoolean(false);

    public AsyncExecutorServiceAdaptor() {
    }

    public void init() {
        int coreSize = Runtime.getRuntime().availableProcessors();
        int maximumPoolSize = Math.round((float)(coreSize * this.radio) / 10.0F);
        if (this.notifyQueueSize < 1) {
            this.notifyQueueSize = 1000;
        }

        int threadQueueSize = coreSize * 200;
        this.maxThreadQueueSize = (long)(threadQueueSize + maximumPoolSize);
        LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue(threadQueueSize);
        RejectedExecutionHandlerImpl handler = new RejectedExecutionHandlerImpl();
        this.executorService = new AsyncCallThreadPoolExecutor(coreSize, maximumPoolSize, this.keepAliveTime, TimeUnit.MILLISECONDS, workQueue, handler);
        handler.setExecutor(this.executorService);
        int maxQueuePoolSize = this.notifyQueueSize * coreSize;
        this.notifyQueue = new NotifyQueue(maxQueuePoolSize);
        this.started.compareAndSet(false, true);
        logger.info("CorePoolSize = " + this.executorService.getCorePoolSize() + " MaximumPoolSize = " + this.executorService.getMaximumPoolSize() + " keepAliveTime = " + this.keepAliveTime + " notifyQueuePoolSize = " + this.notifyQueue.getPoolSize());
    }

    public void destroy() {
        this.started.compareAndSet(true, false);
        logger.warn("ACTP:" + this.executorService.reportThreadPoolStatus());
        List<Runnable> tasks = this.executorService.shutdownNow();
        if (null != tasks) {
            Iterator var2 = tasks.iterator();

            while(var2.hasNext()) {
                Runnable task = (Runnable)var2.next();
                ((AsyncExecutorServiceAdaptor.NotifyWorker)task).backup();
            }
        }

    }

    public <T> Future<T> submit(Callable<T> task) {
        return this.executorService.submit(task);
    }

    public boolean execute(NotifyWorkerCallBack callBack) {
        if (this.isThreadIdle()) {
            AsyncReliableSokulaEvent event = this.notifyQueue.popNext();
            this.executorService.executeTask(new AsyncExecutorServiceAdaptor.NotifyWorker(event, callBack));
            return true;
        } else {
            return false;
        }
    }

    public void setRadio(int notifyRadio) {
        this.radio = notifyRadio;
    }

    public void setNotifyQueueSize(int notifyQueueSize) {
        this.notifyQueueSize = notifyQueueSize;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public AtomicBoolean getStarted() {
        return this.started;
    }

    public NotifyQueue getNotifyQueue() {
        return this.notifyQueue;
    }

    public boolean isWaitIdle() {
        return this.notifyQueue.isWaitIdle();
    }

    public long getThreadIdleSize() {
        long waitSize = this.executorService.getTaskCount() - this.executorService.getCompletedTaskCount();
        return this.maxThreadQueueSize - waitSize;
    }

    public boolean isThreadIdle() {
        long waitSize = this.getThreadIdleSize();
        return waitSize > 0L;
    }

    public String reportExecutorInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("CorePoolSize = ");
        sb.append(this.executorService.getCorePoolSize());
        sb.append(" MaximumPoolSize = ");
        sb.append(this.executorService.getMaximumPoolSize());
        sb.append(" 活动线程数 = ");
        sb.append(this.executorService.getActiveCount());
        sb.append(" 待完成任务数 = ");
        sb.append(this.executorService.getTaskCount() - this.executorService.getCompletedTaskCount());
        sb.append(" 已完成任务数 = ");
        sb.append(this.executorService.getCompletedTaskCount());
        return sb.toString();
    }

    public class NotifyWorker implements Runnable {
        private NotifyWorkerCallBack callBack;
        private AsyncReliableSokulaEvent event;

        public NotifyWorker(AsyncReliableSokulaEvent event, NotifyWorkerCallBack callBack) {
            this.event = event;
            this.callBack = callBack;
        }

        public void run() {
            if (null != this.event) {
                try {
                    String appName = this.event.getAppName();
                    if (StringUtils.isNotBlank(appName)) {
                        String guid = this.event.getGuid();
                        if (ThreadContextUtils.contextInitialized()) {
                            ThreadContextUtils.clearContext();
                        }

                        ThreadContextUtils.initContext(appName, guid, ThreadContextType.TASK);
                    }

                    this.callBack.callBack(this.event);
                } finally {
                    ThreadContextUtils.clearContext();
                }

            }
        }

        public void backup() {
            this.callBack.backup(this.event);
        }
    }
}
