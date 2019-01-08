//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.fstore.impl;

import com.zbjdl.common.utils.event.core.fstore.DataRecoverListener;
import com.zbjdl.common.utils.event.core.fstore.QuickRow;
import com.zbjdl.common.utils.event.core.fstore.Table;
import com.zbjdl.common.utils.event.core.fstore.TableManager;
import com.zbjdl.common.utils.event.core.fstore.TableManagerFactory;
import com.zbjdl.common.utils.event.lang.invoker.SpringInvokerUtils;
import com.zbjdl.common.utils.event.utils.SystemConfigUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableManagerImpl implements TableManager {
    private static final Logger logger = LoggerFactory.getLogger(TableManagerImpl.class);
    private Map<String, TableImpl> tableCache = new HashMap();
    private ReentrantLock lock = new ReentrantLock();
    private AtomicBoolean started = new AtomicBoolean(false);
    public static final long TIME_WINDOW = 120000L;
    private String dataDir;
    private int maxPageDataSize = 20000;
    private long currentTimeWindow = -1L;
    private boolean allLoaded = false;
    private AtomicBoolean recoving = new AtomicBoolean(false);
    private static int count = 1;
    public static int TABLE_SIZE = 8;

    public TableManagerImpl() {
    }

    public void init() {
        if (this.started.compareAndSet(false, true)) {
            logger.info("start tableManager! [BEGIN]");
            this.dataDir = SystemConfigUtils.getProperty("file.storage.data.dir", (String)null);
            String root;
            if (StringUtils.isBlank(this.dataDir)) {
                root = this.getRootDir();
                if (root.endsWith("/")) {
                    this.dataDir = root + ".zbjdl-event";
                } else {
                    this.dataDir = root + "/.zbjdl-event";
                }

                String contextName = SpringInvokerUtils.getContextName();
                if (StringUtils.isNotBlank(contextName)) {
                    this.dataDir = this.dataDir + "/" + contextName;
                }
            } else if (StringUtils.startsWith(this.dataDir, "~")) {
                root = this.getRootDir();
                if (root.endsWith("/")) {
                    this.dataDir = this.dataDir.replace("~/", root);
                } else {
                    this.dataDir = this.dataDir.replace("~", root);
                }
            }

            logger.info("event dataDir : " + this.dataDir);
            this.loadTable();
            TableManagerFactory.registerTableManager(this);
        }

    }

    private String getRootDir() {
        String dataDir = System.getProperty("user.home");
        if (StringUtils.isBlank(dataDir)) {
            dataDir = "/apps/data/java-tmpdir/";
        }

        return dataDir;
    }

    public Table findTableForWrite() {
        int ext = count++;
        if (count > TABLE_SIZE) {
            count = 1;
        }

        String tableName = "fsdb2_" + ext;
        return this.findTable(tableName);
    }

    public void recoverData(DataRecoverListener listener) {
        if (this.recoving.compareAndSet(false, true)) {
            try {
                Collection<String> tableNames = this.tableNames();
                if (null != tableNames && tableNames.size() > 0) {
                    long nextTimeWindow = System.currentTimeMillis() + 120000L;
                    nextTimeWindow -= nextTimeWindow % 120000L;
                    if (this.currentTimeWindow != nextTimeWindow || !this.allLoaded) {
                        if (this.currentTimeWindow != nextTimeWindow) {
                            this.resetTimeWindow(nextTimeWindow);
                        }

                        Iterator var5 = tableNames.iterator();

                        while(var5.hasNext()) {
                            String tableName = (String)var5.next();
                            Table table = this.findTable(tableName);
                            if (table.requireAdminToken()) {
                                try {
                                    List<QuickRow> rows = table.getDataList(nextTimeWindow);
                                    if (logger.isInfoEnabled() && rows.size() > 0) {
                                        logger.info("TableManagerImpl recoverData : TableName = " + tableName + " | rows = " + rows.size());
                                    }

                                    Iterator var9 = rows.iterator();

                                    while(var9.hasNext()) {
                                        QuickRow row = (QuickRow)var9.next();
                                        if (!listener.recoverData(row)) {
                                            this.allLoaded = false;
                                            return;
                                        }
                                    }

                                    try {
                                        table.rotatePage();
                                    } catch (Exception var19) {
                                        logger.error("rotatePage Error:", var19);
                                    }
                                } finally {
                                    table.releaseAdminToken();
                                }
                            }
                        }

                        this.allLoaded = true;
                    }
                }
            } finally {
                this.recoving.set(false);
            }
        }
    }

    public void cleanTable() {
        Collection<String> tableNames = this.tableNames();
        if (null != tableNames && tableNames.size() > 0) {
            Iterator var2 = tableNames.iterator();

            while(var2.hasNext()) {
                String tableName = (String)var2.next();
                Table table = this.findTable(tableName);
                if (table.requireAdminToken()) {
                    try {
                        table.cleanTableSpace();
                    } finally {
                        table.releaseAdminToken();
                    }
                }
            }
        }

    }

    public Table findTable(String tableName) {
        return this.findTable(tableName, this.maxPageDataSize, -1);
    }

    public Table findTable(String tableName, int pageSize, int cachedDataSize) {
        if (this.tableCache.containsKey(tableName)) {
            return (Table)this.tableCache.get(tableName);
        } else {
            this.lock.lock();

            TableImpl var5;
            try {
                if (this.tableCache.containsKey(tableName)) {
                    Table var11 = (Table)this.tableCache.get(tableName);
                    return var11;
                }

                TableImpl table = new TableImpl(this.dataDir, tableName);
                table.setMaxPageDataSize(pageSize);
                table.start();
                this.tableCache.put(tableName, table);
                var5 = table;
            } catch (Exception var9) {
                logger.error("", var9);
                return null;
            } finally {
                this.lock.unlock();
            }

            return var5;
        }
    }

    public Collection<String> tableNames() {
        return new ArrayList(this.tableCache.keySet());
    }

    public void destroy() {
        Collection<TableImpl> values = this.tableCache.values();
        if (null != values) {
            Iterator var2 = values.iterator();

            while(var2.hasNext()) {
                TableImpl table = (TableImpl)var2.next();

                try {
                    table.stop();
                } catch (Exception var5) {
                    logger.error("", var5);
                }
            }
        }

        this.started.compareAndSet(true, false);
        logger.info("stop tableManager! [DONE] ");
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public void setMaxPageDataSize(int maxPageDataSize) {
        this.maxPageDataSize = maxPageDataSize;
    }

    private void loadTable() {
        File file = new File(this.dataDir);
        if (!file.exists()) {
            file.mkdir();
        }

        String[] fileNames = file.list();
        if (fileNames != null) {
            String[] var3 = fileNames;
            int var4 = fileNames.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String tableName = var3[var5];
                logger.info("LOAD TABLE : " + tableName);
                this.findTable(tableName);
            }

        }
    }

    private void resetTimeWindow(long nextTimeWindow) {
        this.currentTimeWindow = nextTimeWindow;
        this.allLoaded = false;
    }
}
