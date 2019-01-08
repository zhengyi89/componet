//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.notify;

import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.core.fstore.DataRecoverListener;
import com.zbjdl.common.utils.event.core.fstore.QuickLocation;
import com.zbjdl.common.utils.event.core.fstore.Table;
import com.zbjdl.common.utils.event.core.fstore.TableManager;
import com.zbjdl.common.utils.event.core.fstore.TableManagerFactory;
import com.zbjdl.common.utils.event.core.thread.NotifyWorkerCallBack;
import com.zbjdl.common.utils.event.exception.EventRuntimeException;
import com.zbjdl.common.utils.event.impl.EventEngineImpl;
import com.zbjdl.common.utils.event.impl.InvokeModel;
import com.zbjdl.common.utils.event.impl.InvokeModelEnum;
import com.zbjdl.common.utils.event.impl.call.AbstractBrokerImpl;
import com.zbjdl.common.utils.event.lang.diagnostic.Profiler;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class NotifyBrokerImpl extends AbstractBrokerImpl implements InvokeModel, NotifyStatusEventListener, NotifyWorkerCallBack {
    private static final Logger logger = LoggerFactory.getLogger(NotifyBrokerImpl.class);
    private static final Logger loggerDeadEvent = LoggerFactory.getLogger("SYS.DEAD.EVENT");
    private static final Logger loggerPerf = LoggerFactory.getLogger("SYS.PERF");
    private NotifyExecutorService notifyExecutorService;
    private DataRecoverListener notifyDataRecover;
    private NotifyDelayStrategy notifyDelayStrategy;

    public NotifyBrokerImpl() {
    }

    public boolean accept(InvokeModelEnum type) {
        return type == InvokeModelEnum.NOTIFY;
    }

    public Object broker(SokulaEvent event, CountDownLatch doneSignal) {
        try {
            boolean inTransaction = event.isInTransaction();
            QuickLocation location;
            if (inTransaction && TransactionSynchronizationManager.isActualTransactionActive()) {
                location = this.notifyDelayStrategy.saveEvent(-1, -1, event, true);
                TransactionSynchronizationManager.registerSynchronization(new NotifyBrokerImpl.EventTransactionSynchronization(location, event));
            } else {
                location = this.notifyDelayStrategy.saveEvent(-1, -1, event, false);
                location.setGmtModify(event.getDelayHandleTime());
                this.handleEvent(new AsyncReliableSokulaEvent(event, location, 1));
            }

            return null;
        } catch (Exception var5) {
            logger.error("save event error. event : " + event, var5);
            throw new EventRuntimeException(77777, var5);
        }
    }

    public void handleEvent(AsyncReliableSokulaEvent event) {
        switch(event.getEventType()) {
        case 1:
            this.handleNewEvent(event);
        case 2:
        case 6:
        case 8:
        case 9:
        case 10:
        case 12:
        case 14:
        case 16:
        default:
            break;
        case 3:
            this.handleRecoverEvent(event);
            break;
        case 4:
            this.handleInterceptEvent(event);
            break;
        case 5:
            this.handleThreadIdleEvent();
            break;
        case 7:
            this.handleWaitIdleEvent();
            break;
        case 11:
            this.handleSuccessEvent(event);
            break;
        case 13:
            this.handleFailureEvent(event);
            break;
        case 15:
            this.handleDeadEvent(event);
            break;
        case 17:
            this.handleRejectEvent(event);
        }

    }

    private void handleNewEvent(AsyncReliableSokulaEvent event) {
        if (event.getStatus() != AsyncStatusEnum.FILE_QUEUE) {
            logger.error("BIZ:NEW--Event status is not FILE_QUEUE. status = " + event.getStatus());
        } else {
            if (event.isInHandleWindow()) {
                NotifyPushEnum rt = this.notifyExecutorService.getNotifyQueue().pushNew(event);
                if (rt == NotifyPushEnum.WAITING) {
                    this.notifyByExecutor();
                }
            }

        }
    }

    private void handleRecoverEvent(AsyncReliableSokulaEvent event) {
        if (event.getStatus() != AsyncStatusEnum.FILE_QUEUE) {
            logger.error("BIZ:RECOVER--Event status is not FILE_QUEUE. status = " + event.getStatus());
        } else {
            NotifyPushEnum rt = this.notifyExecutorService.getNotifyQueue().pushRecovery(event);
            if (rt != NotifyPushEnum.WAIT_FULL && rt != NotifyPushEnum.WORK_FULL && rt != NotifyPushEnum.IGNORED) {
                this.notifyDelayStrategy.delay(event, 2, false);
                if (rt == NotifyPushEnum.WAITING) {
                    this.notifyByExecutor();
                }

            }
        }
    }

    private void handleSuccessEvent(AsyncReliableSokulaEvent event) {
        if (event.getStatus() != AsyncStatusEnum.HANDLED_SUCCESS) {
            logger.error("BIZ:SUCCESS--Event status is not HANDLED_SUCCESS. status = " + event.getStatus());
        } else {
            try {
                TableManager tableManager = TableManagerFactory.findTableManager();
                Table table = tableManager.findTable(event.getLocation().getTableName());
                table.remove(event.getLocation());
            } catch (Exception var4) {
                logger.error("remove msg error , location --| " + event.getLocation().toURL() + "|--", var4);
            }

            this.notifyExecutorService.getNotifyQueue().popSuccess(event);
            this.handleNextEvent();
        }
    }

    private void handleFailureEvent(AsyncReliableSokulaEvent event) {
        if (event.getStatus() != AsyncStatusEnum.HANDLED_FAILURE) {
            logger.error("BIZ:FAILURE--Event status is not HANDLED_FAILURE. status = " + event.getStatus());
        } else {
            if (this.notifyDelayStrategy.canRecur(event)) {
                this.notifyExecutorService.getNotifyQueue().popFailureRecur(event);
            } else {
                this.notifyExecutorService.getNotifyQueue().popFailureDrop(event);
            }

            this.handleNextEvent();
        }
    }

    private void handleDeadEvent(AsyncReliableSokulaEvent event) {
        if (event.getStatus() != AsyncStatusEnum.HANDLED_FAILURE) {
            logger.error("BIZ:FAILURE--Event status is not HANDLED_FAILURE. status = " + event.getStatus());
        } else {
            try {
                loggerDeadEvent.warn("DEAD EVENT !! location --|" + event.getLocation().toURL() + "| eventId = " + event.getEventId());
                TableManager tableManager = TableManagerFactory.findTableManager();
                Table table = tableManager.findTable(event.getLocation().getTableName());
                table.dead(event.getLocation());
            } catch (Exception var4) {
                logger.error("resend msg error , location --| " + event.getLocation().toURL() + "|--", var4);
            }

            this.notifyExecutorService.getNotifyQueue().popFailureDrop(event);
            this.handleNextEvent();
        }
    }

    private void handleInterceptEvent(AsyncReliableSokulaEvent event) {
        logger.info("BIZ:RECOVERDNEW--Event status is NEW,wait to commit. eventId = " + event.getEventId());
        this.notifyExecutorService.getNotifyQueue().popFailureDrop(event);
        this.handleNextEvent();
    }

    private void handleThreadIdleEvent() {
        long threadIdleSize = this.notifyExecutorService.getThreadIdleSize();
        long waitQueueSize = (long)this.notifyExecutorService.getNotifyQueue().getWaitSize();
        long loopCount = waitQueueSize <= threadIdleSize ? waitQueueSize : threadIdleSize;

        for(long i = 0L; i < loopCount; ++i) {
            this.notifyByExecutor();
        }

    }

    private void handleWaitIdleEvent() {
        TableManager tableManager = TableManagerFactory.findTableManager();
        tableManager.recoverData(this.notifyDataRecover);
    }

    private void handleNextEvent() {
        if (this.notifyExecutorService.getNotifyQueue().hasTask()) {
            this.notifyByExecutor();
        }

    }

    private void handleRejectEvent(AsyncReliableSokulaEvent event) {
        if (event.getStatus() != AsyncStatusEnum.REJECTED) {
            logger.error("BIZ:REJECTED--Event status is not REJECTED. status = " + event.getStatus());
        } else {
            this.notifyDelayStrategy.delayAndRefresh(event, 1, false);
            if (this.notifyDelayStrategy.canRecur(event)) {
                this.notifyExecutorService.getNotifyQueue().popFailureRecur(event);
            } else {
                this.notifyExecutorService.getNotifyQueue().popFailureDrop(event);
            }

            this.notifyByExecutor();
        }
    }

    private void notifyByExecutor() {
        this.notifyExecutorService.execute(this);
    }

    public void setNotifyDataRecover(DataRecoverListener notifyDataRecover) {
        this.notifyDataRecover = notifyDataRecover;
    }

    public void setNotifyExecutorService(NotifyExecutorService notifyExecutorService) {
        this.notifyExecutorService = notifyExecutorService;
    }

    public void setNotifyDelayStrategy(NotifyDelayStrategy notifyDelayStrategy) {
        this.notifyDelayStrategy = notifyDelayStrategy;
    }

    public void callBack(AsyncReliableSokulaEvent event) {
        Profiler.start("NotifyWorker.run | " + event.getEventId());
        boolean var11 = false;

        long elapseTime;
        StringBuilder builder;
        label104: {
            try {
                try {
                    var11 = true;
                    event.setStatus(AsyncStatusEnum.WORKING);
                    this.eventCall(event);
                    event.setStatus(AsyncStatusEnum.HANDLED_SUCCESS);
                    event.setEventType(11);
                    this.handleEvent(event);
                    var11 = false;
                    break label104;
                } catch (Throwable e) {
                    event.setStatus(AsyncStatusEnum.HANDLED_FAILURE);
                    if (this.notifyDelayStrategy.isNew(event, e)) {
                        event.setEventType(4);
                    } else if (this.notifyDelayStrategy.isDead(event, e)) {
                        event.setEventType(15);
                    } else {
                        event.setEventType(13);
                        this.notifyDelayStrategy.delay(event, e);
                    }
                    logger.error("NotifyWorker:" + e.getMessage());
                }

                this.handleEvent(event);
               
                var11 = false;
            } finally {
                if (var11) {
                    Profiler.release();
                    long elapseTime1 = Profiler.getDuration();
                    if (elapseTime1 > (long)EventEngineImpl.THRESHOLD) {
                        StringBuilder builder1 = new StringBuilder();
                        builder1.append("URL:").append(event.getEventName());
                        builder1.append(" over PMX = ").append(EventEngineImpl.THRESHOLD).append("ms,");
                        builder1.append(" used P = ").append(elapseTime1).append("ms.\r\n");
                        builder1.append(Profiler.dump(EventEngineImpl.PERF_PREFIX));
                        loggerPerf.info(builder1.toString());
                    }

                    Profiler.reset();
                }
            }

            Profiler.release();
            elapseTime = Profiler.getDuration();
            if (elapseTime > (long)EventEngineImpl.THRESHOLD) {
                builder = new StringBuilder();
                builder.append("URL:").append(event.getEventName());
                builder.append(" over PMX = ").append(EventEngineImpl.THRESHOLD).append("ms,");
                builder.append(" used P = ").append(elapseTime).append("ms.\r\n");
                builder.append(Profiler.dump(EventEngineImpl.PERF_PREFIX));
                loggerPerf.info(builder.toString());
            }

            Profiler.reset();
            return;
        }

        Profiler.release();
        elapseTime = Profiler.getDuration();
        if (elapseTime > (long)EventEngineImpl.THRESHOLD) {
            builder = new StringBuilder();
            builder.append("URL:").append(event.getEventName());
            builder.append(" over PMX = ").append(EventEngineImpl.THRESHOLD).append("ms,");
            builder.append(" used P = ").append(elapseTime).append("ms.\r\n");
            builder.append(Profiler.dump(EventEngineImpl.PERF_PREFIX));
            loggerPerf.info(builder.toString());
        }

        Profiler.reset();
    }

    public void backup(AsyncReliableSokulaEvent arevent) {
        arevent.setStatus(AsyncStatusEnum.REJECTED);
        arevent.setEventType(17);
        this.handleEvent(arevent);
        if (logger.isDebugEnabled()) {
            logger.error("Event is rejected. event : " + arevent);
        }

    }

    public class EventTransactionSynchronization extends TransactionSynchronizationAdapter {
        private QuickLocation location;
        private SokulaEvent event;

        public EventTransactionSynchronization(QuickLocation location, SokulaEvent event) {
            this.location = location;
            this.event = event;
        }

        public void afterCompletion(int status) {
            try {
                TableManager tableManager = TableManagerFactory.findTableManager();
                Table table = tableManager.findTable(this.location.getTableName());
                if (status == 0) {
                    table.commit(this.location);
                    this.location.setGmtModify(this.event.getDelayHandleTime());
                    NotifyBrokerImpl.this.handleEvent(new AsyncReliableSokulaEvent(this.event, this.location, 1));
                } else {
                    table.rollback(this.location);
                }
            } catch (Exception var4) {
                NotifyBrokerImpl.logger.error("", var4);
            }

        }
    }
}
