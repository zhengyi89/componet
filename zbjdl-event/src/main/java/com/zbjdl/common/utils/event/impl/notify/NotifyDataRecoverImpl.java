//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.notify;

import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.core.fstore.DataRecoverListener;
import com.zbjdl.common.utils.event.core.fstore.QuickRow;
import com.zbjdl.common.utils.event.core.fstore.Table;
import com.zbjdl.common.utils.event.core.fstore.TableManager;
import com.zbjdl.common.utils.event.core.fstore.TableManagerFactory;
import com.zbjdl.common.utils.event.utils.HessianSerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyDataRecoverImpl implements DataRecoverListener {
    private static final Logger logger = LoggerFactory.getLogger(NotifyDataRecoverImpl.class);
    private NotifyExecutorService notifyExecutorService;
    private NotifyStatusEventSupport notifyStatusEventSupport;

    public NotifyDataRecoverImpl() {
    }

    public boolean recoverData(QuickRow row) {
        if (row == null) {
            return true;
        } else {
            Object obj = null;

            try {
                TableManager tableManager = TableManagerFactory.findTableManager();
                Table table = tableManager.findTable(row.getKey().getTableName());
                if (row.getKey().getGmtModify() > NotifyDelayStrategy.getNextTimeWindow()) {
                    return true;
                }

                obj = HessianSerUtils.deserialize(row.getValue());
                if (null == obj || obj instanceof String) {
                    logger.error("FILE: DATA IS Dead , key = " + row.getKey().toURL() + " | value =" + obj);
                    table.dead(row.getKey());
                    return true;
                }

                SokulaEvent event = (SokulaEvent)obj;
                if (logger.isDebugEnabled()) {
                    logger.debug("NotifyDataRecoverImpl : " + event);
                }

                if (!this.notifyExecutorService.isWaitIdle()) {
                    return false;
                }

                this.notifyStatusEventSupport.fireEvent(new AsyncReliableSokulaEvent(event, row.getKey(), 3));
            } catch (Throwable var6) {
                logger.error("tableName =" + row.getKey().getTableName() + " | obj = " + obj, var6);
            }

            return true;
        }
    }

    public void setNotifyExecutorService(NotifyExecutorService notifyExecutorService) {
        this.notifyExecutorService = notifyExecutorService;
    }

    public void setNotifyStatusEventSupport(NotifyStatusEventSupport notifyStatusEventSupport) {
        this.notifyStatusEventSupport = notifyStatusEventSupport;
    }
}
