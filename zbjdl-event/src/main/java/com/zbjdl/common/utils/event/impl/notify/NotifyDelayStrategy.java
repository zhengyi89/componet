//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.notify;

import com.caucho.hessian.client.HessianRuntimeException;
import com.zbjdl.common.exception.BaseException;
import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.core.fstore.QuickLocation;
import com.zbjdl.common.utils.event.core.fstore.Table;
import com.zbjdl.common.utils.event.core.fstore.TableManager;
import com.zbjdl.common.utils.event.core.fstore.TableManagerFactory;
import com.zbjdl.common.utils.event.exception.EventRuntimeException;
import com.zbjdl.common.utils.event.utils.DateUtils;
import com.zbjdl.common.utils.event.utils.HessianSerUtils;
import java.util.Date;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyDelayStrategy {
    private static final Logger logger = LoggerFactory.getLogger(NotifyDelayStrategy.class);
    public static final long TIME_WINDOW = 120000L;
    public static final long DELAY_WINDOW = 5000L;

    public NotifyDelayStrategy() {
    }

    public QuickLocation saveEvent(int pageSize, int cachedDataSize, SokulaEvent event, boolean inTransaction) throws Exception {
        TableManager tableManager = TableManagerFactory.findTableManager();
        if (null == tableManager) {
            throw new EventRuntimeException(97100001, "File Storage Don't Inited!");
        } else {
            Table table = tableManager.findTableForWrite();
            byte[] value = HessianSerUtils.serialize(event);
            long delayHandleTime = this.getNextHanlderTime(event.getDelayHandleTime(), 2);
            QuickLocation ql = table.store(event.getEventId(), value, delayHandleTime, inTransaction);
            logger.debug("SAVE: ID=" + event.getEventId() + " | C = " + DateUtils.format(new Date(event.getDelayHandleTime()), DateUtils.newFormat) + " | N = " + DateUtils.format(new Date(delayHandleTime), DateUtils.newFormat));
            return ql;
        }
    }

    public int delay(AsyncReliableSokulaEvent event, int delayWindow, boolean isException) {
        try {
            long nextTimeWindow = getNextTimeWindow();
            if (event.getLocation().getGmtModify() > nextTimeWindow) {
                return 0;
            } else {
                TableManager tableManager = TableManagerFactory.findTableManager();
                Table table = tableManager.findTable(event.getLocation().getTableName());
                int realDelayWindow = delayWindow;
                if (isException) {
                    int errCount = event.getLocation().getErrCount();
                    realDelayWindow = delayWindow + errCount;
                    event.getLocation().setErrCount(errCount + 1);
                }

                long nextHandleTime = this.getNextHanlderTime(event.getDelayHandleTime(), realDelayWindow);
                logger.debug("DELAY: ID=" + event.getEventId() + " | C = " + DateUtils.format(new Date(event.getDelayHandleTime()), DateUtils.newFormat) + " | N = " + DateUtils.format(new Date(nextHandleTime), DateUtils.newFormat) + " | W = " + delayWindow);
                event.getLocation().setGmtModify(nextHandleTime);
                table.sended(event.getLocation());
                return realDelayWindow;
            }
        } catch (Exception var11) {
            logger.error("resend msg error , location --| " + event.getLocation().toURL() + "|--", var11);
            return -1;
        }
    }

    public void delay(AsyncReliableSokulaEvent event, Throwable thr) {
        int delayWindow;
        boolean isRealException = true;
        if (thr instanceof EventRuntimeException) {
            EventRuntimeException e = (EventRuntimeException)thr;
            if (e.getErrorCode() == 99201005) {
                delayWindow = 1;
                isRealException = false;
            } else if (e.getErrorCode() != 99201006 && e.getErrorCode() != 99001003) {
                delayWindow = 10;
            } else {
                delayWindow = 2;
            }
        } else if (thr instanceof BaseException) {
            delayWindow = 5;
        } else if (thr instanceof HessianRuntimeException) {
            delayWindow = 2;
        } else {
            Throwable root = ExceptionUtils.getRootCause(thr);
            if (root != null) {
                this.delay(event, root);
                return;
            }

            delayWindow = 10;
        }

        if (delayWindow > 0) {
            this.delayAndRefresh(event, delayWindow, isRealException);
        }

    }

    public void delayAndRefresh(AsyncReliableSokulaEvent event, int delayWindow, boolean isException) {
        int realDelayWindow = this.delay(event, delayWindow, isException);
        if (realDelayWindow == 1) {
            event.setDelayHandleTime(System.currentTimeMillis());
        } else {
            event.setDelayHandleTime(event.getLocation().getGmtModify());
        }

    }

    private long getNextHanlderTime(long currentHandleTime, int delayWindow) {
        return getNextTimeWindow() + 120000L * (long)(delayWindow - 1) + 5000L;
    }

    public boolean isNew(AsyncReliableSokulaEvent event, Throwable thr) {
        if (thr instanceof EventRuntimeException) {
            EventRuntimeException e = (EventRuntimeException)thr;
            if (e.getErrorCode() == 99201008) {
                return true;
            }
        }

        return false;
    }

    public boolean isDead(AsyncReliableSokulaEvent event, Throwable thr) {
        if (thr instanceof EventRuntimeException) {
            EventRuntimeException e = (EventRuntimeException)thr;
            if (e.getErrorCode() == 99201007) {
                return true;
            }
        } else {
            if (thr instanceof BaseException) {
                return true;
            }

            Throwable root = ExceptionUtils.getRootCause(thr);
            if (root instanceof BaseException) {
                return true;
            }
        }

        return false;
    }

    public static long getNextTimeWindow() {
        long nextTimeWindow = System.currentTimeMillis() + 120000L;
        nextTimeWindow -= nextTimeWindow % 120000L;
        return nextTimeWindow;
    }

    public boolean canRecur(AsyncReliableSokulaEvent event) {
        return event.canRecur();
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.format(new Date(System.currentTimeMillis()), DateUtils.newFormat));

        for(int i = 0; i < 10; ++i) {
            long nextTimeWindow = System.currentTimeMillis() + 120000L;
            nextTimeWindow -= nextTimeWindow % 120000L;
            System.out.println(DateUtils.format(new Date(nextTimeWindow), DateUtils.newFormat));

            try {
                Thread.sleep(10000L);
            } catch (Exception var5) {
                ;
            }
        }

    }
}
