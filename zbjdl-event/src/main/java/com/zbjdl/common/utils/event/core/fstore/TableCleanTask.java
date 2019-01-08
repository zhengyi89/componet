//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.fstore;

import com.zbjdl.common.utils.event.utils.ScheduledTaskUtils;
import com.zbjdl.common.utils.event.utils.SystemConfigUtils;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableCleanTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(TableCleanTask.class);
    private AtomicBoolean taskState = new AtomicBoolean(false);

    public TableCleanTask() {
    }

    public void init() {
        int interval = SystemConfigUtils.getIntProperty("file.storage.data.clean.minute", 5);
        if (interval < 1) {
            interval = 1;
        }

        ScheduledTaskUtils.scheduleWithFixedDelay(this, 5000L, (long)('\uea60' * interval));
    }

    public void destroy() {
    }

    public void run() {
        if (this.taskState.compareAndSet(false, true)) {
            try {
                TableManager tableManager = TableManagerFactory.findTableManager();
                tableManager.cleanTable();
            } catch (Throwable var5) {
                logger.error("exception.", var5);
            } finally {
                this.taskState.set(false);
            }
        }

    }
}
