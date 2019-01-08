//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.notify;

import com.zbjdl.common.utils.event.utils.ScheduledTaskUtils;
import com.zbjdl.common.utils.event.utils.SystemConfigUtils;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyQueueMonitor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(NotifyQueueMonitor.class);
    private AsyncReliableSokulaEvent THREAD_IDLE_EVENT = new AsyncReliableSokulaEvent(5);
    private AsyncReliableSokulaEvent WAIT_QUEUE_IDLE_EVENT = new AsyncReliableSokulaEvent(7);
    private AtomicBoolean taskStatus = new AtomicBoolean(false);
    private NotifyExecutorService notifyExecutorService;
    private NotifyStatusEventSupport notifyStatusEventSupport;
    private boolean monitorLog = false;

    public NotifyQueueMonitor() {
    }

    public void init() {
        int schedule = SystemConfigUtils.getIntProperty("event.monitor.schedule", 2);
        this.monitorLog = SystemConfigUtils.getBooleanProperty("event.monitor.log", false);
        ScheduledTaskUtils.scheduleWithFixedDelay(this, 60000L, (long)(1000 * schedule));
    }

    public void destroy() {
    }

    public void run() {
        if (this.taskStatus.compareAndSet(false, true)) {
            try {
                if (this.notifyExecutorService.isThreadIdle()) {
                    this.notifyStatusEventSupport.fireEvent(this.THREAD_IDLE_EVENT);
                }

                if (this.notifyExecutorService.isWaitIdle()) {
                    this.notifyStatusEventSupport.fireEvent(this.WAIT_QUEUE_IDLE_EVENT);
                }
            } catch (Exception var5) {
                logger.error("", var5);
            } finally {
                this.taskStatus.set(false);
            }
        }

        if (this.monitorLog) {
            NotifyQueue notifyQueue = this.notifyExecutorService.getNotifyQueue();
            logger.info("等待队列任务数 : " + notifyQueue.getWaitSize() + " | 执行队列任务数: " + notifyQueue.getWorkSize() + " | 缓存池事件数: " + notifyQueue.getPoolSize() + " | 执行器信息 : " + this.notifyExecutorService.reportExecutorInfo());
        }

    }

    public void setNotifyExecutorService(NotifyExecutorService notifyExecutorService) {
        this.notifyExecutorService = notifyExecutorService;
    }

    public void setNotifyStatusEventSupport(NotifyStatusEventSupport notifyStatusEventSupport) {
        this.notifyStatusEventSupport = notifyStatusEventSupport;
    }
}
