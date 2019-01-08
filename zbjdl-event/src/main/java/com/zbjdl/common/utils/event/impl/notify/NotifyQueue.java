//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.notify;

import java.io.Serializable;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyQueue implements Serializable {
    private static final long serialVersionUID = 6638133208589534879L;
    private static final Logger logger = LoggerFactory.getLogger(NotifyQueue.class);
    private static final double PERCENT_WAIT_BUSY = 0.8D;
    private int poolSize = -1;
    private Queue<String> waitList;
    private Queue<String> workList;
    private Map<String, AsyncReliableSokulaEvent> eventPool;
    private transient int waitBusyRadio = -1;
    private AtomicLong inCounter = new AtomicLong(0L);
    private AtomicLong outCounter = new AtomicLong(0L);

    public NotifyQueue(int poolSize) {
        this.poolSize = poolSize;
        this.waitBusyRadio = (int)((double)poolSize * 0.8D);
        this.waitList = new ConcurrentLinkedQueue();
        this.workList = new ConcurrentLinkedQueue();
        this.eventPool = new ConcurrentHashMap(this.poolSize);
    }

    public NotifyPushEnum pushNew(AsyncReliableSokulaEvent event) {
        NotifyPushEnum rt = this.addToWait(event);
        return rt;
    }

    public NotifyPushEnum pushRecovery(AsyncReliableSokulaEvent event) {
        NotifyPushEnum rt = null;
        if (this.waitBusyRadio > 0 && this.waitList.size() > this.waitBusyRadio) {
            rt = NotifyPushEnum.WAIT_FULL;
        } else {
            rt = this.addToWait(event);
        }

        logger.info("RECOVERY: " + event.getEventId() + " | EventType = " + event.getEventType() + " | status = " + event.getLocation().getStatus() + " | rtEnum = " + rt.name());
        return rt;
    }

    public void popSuccess(AsyncReliableSokulaEvent event) {
        this.removeEvent(event);
    }

    public void popFailureDrop(AsyncReliableSokulaEvent event) {
        this.removeEvent(event);
        logger.info("DROP : " + event.getEventId() + " | status = " + event.getEventType());
    }

    public void popFailureRecur(AsyncReliableSokulaEvent event) {
        logger.info("RECUR : " + event.getEventId() + " | EventType = " + event.getEventType());
        this.removeEvent(event);
        this.addToWait(event);
    }

    public boolean hasTask() {
        return !this.waitList.isEmpty();
    }

    public synchronized AsyncReliableSokulaEvent popNext() {
        if (this.waitList.isEmpty()) {
            return null;
        } else {
            String eventId = (String)this.waitList.poll();
            if (eventId == null) {
                return null;
            } else {
                AsyncReliableSokulaEvent event = (AsyncReliableSokulaEvent)this.eventPool.get(eventId);
                if (event != null) {
                    if (event.canHandle()) {
                        event.setStatus(AsyncStatusEnum.QUEUEING);
                        this.workList.add(eventId);
                        return event;
                    }

                    this.waitList.add(eventId);
                }

                return null;
            }
        }
    }

    public AsyncReliableSokulaEvent getEvent(String eventId) {
        return (AsyncReliableSokulaEvent)this.eventPool.get(eventId);
    }

    private synchronized NotifyPushEnum addToWait(AsyncReliableSokulaEvent event) {
        String eventId = event.getEventId();
        if (this.eventPool.size() >= this.poolSize) {
            return NotifyPushEnum.IGNORED;
        } else {
            boolean poolExist = this.eventPool.containsKey(eventId);
            if (poolExist) {
                if (!this.workList.contains(eventId) && !this.waitList.contains(eventId)) {
                    this.waitList.add(eventId);
                    return NotifyPushEnum.WAITING;
                } else {
                    return NotifyPushEnum.IGNORED;
                }
            } else {
                event.setStatus(AsyncStatusEnum.WAIT_QUEUE);
                this.waitList.add(eventId);
                this.eventPool.put(eventId, event);
                this.inCounter.incrementAndGet();
                return NotifyPushEnum.WAITING;
            }
        }
    }

    private synchronized void removeEvent(AsyncReliableSokulaEvent event) {
        this.eventPool.remove(event.getEventId());
        this.workList.remove(event.getEventId());
        this.waitList.remove(event.getEventId());
        this.outCounter.incrementAndGet();
    }

    public int getWaitSize() {
        return this.waitList.size();
    }

    public int getWorkSize() {
        return this.workList.size();
    }

    public int getPoolSize() {
        return this.eventPool.size();
    }

    public boolean isWaitIdle() {
        if (this.waitBusyRadio <= 0 && this.waitList.size() < this.poolSize) {
            return true;
        } else {
            return this.waitList.size() < this.waitBusyRadio;
        }
    }

    public String toString() {
        try {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        } catch (Exception var2) {
            return super.toString();
        }
    }
}
