//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.fstore.impl;

import com.zbjdl.common.utils.event.core.fstore.QuickLocation;
import com.zbjdl.common.utils.event.core.fstore.QuickRow;
import com.zbjdl.common.utils.event.core.fstore.Table;
import com.zbjdl.common.utils.event.core.fstore.impl.TablePage.Key;
import com.zbjdl.common.utils.event.utils.DateUtils;
import com.zbjdl.common.utils.event.utils.SystemConfigUtils;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableImpl implements Table {
    private static final Logger logger = LoggerFactory.getLogger(TableImpl.class);
    public static final int NEW = 0;
    public static final int COMMIT = 1;
    public static final int ROLLBACK = 2;
    public static final int REMOVED = 3;
    public static final int DEAD = 4;
    private static final String EVENT_PAGE_DATA_SIZE = "file.storage.data.page.size";
    private String dataDir;
    private String tableName;
    private TablePage currentTablePage;
    private int maxPageDataSize = 10000;
    private long beginWriteTime = System.currentTimeMillis();
    public static int RECORD_SIZE = 1280;
    private Map<String, TablePage> tablePageCache = new ConcurrentHashMap();
    private AtomicBoolean started = new AtomicBoolean();
    private AtomicInteger counter = new AtomicInteger(0);
    private int pageCounter = 0;
    private String date;
    private Date currentDate = new Date();
    private ReentrantLock lock = new ReentrantLock();
    private String currentFileName = "_NULL_";
    private String tableDirName;
    private static long FILE_KEEP_TIME = 900000L;
    private AtomicBoolean adminToken = new AtomicBoolean(false);

    public TableImpl(String dataDir, String tableName) {
        this.dataDir = dataDir;
        this.tableName = tableName;
        this.tableDirName = dataDir + "/" + tableName + "/";
        int pds = SystemConfigUtils.getIntProperty("file.storage.data.page.size", this.maxPageDataSize);
        this.setMaxPageDataSize(pds);
    }

    public void setMaxPageDataSize(int maxPageDataSize) {
        if (maxPageDataSize > 100) {
            this.maxPageDataSize = maxPageDataSize;
        }

    }

    public void start() throws Exception {
        if (this.started.compareAndSet(false, true)) {
            logger.info("TABLE START ! [BEGIN] " + this.tableName);
            this.calculatePageCounter();
            if (this.tableName.startsWith("fsdb2_")) {
                this.currentTablePage = this.initCurrentPage();
            }
        }

    }

    private void calculatePageCounter() {
        String tempDataDir = this.getCurrentDayQueueDir();
        File file = new File(tempDataDir);
        if (file.exists()) {
            String[] fileNames = file.list();
            if (null != fileNames && fileNames.length > 0) {
                int tempLong = 0;
                String[] var5 = fileNames;
                int var6 = fileNames.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    String fileName = var5[var7];
                    int p = fileName.indexOf(".");
                    fileName = fileName.substring(0, p);
                    int tt = NumberUtils.toInt(fileName);
                    if (tt > tempLong) {
                        tempLong = tt;
                    }
                }

                this.pageCounter = tempLong - 10000 + 1;
            }
        } else {
            file.mkdirs();
        }

    }

    public void stop() throws Exception {
        if (this.started.compareAndSet(true, false)) {
            Collection<TablePage> values = this.tablePageCache.values();
            Iterator var2 = values.iterator();

            while(var2.hasNext()) {
                TablePage tablePage = (TablePage)var2.next();
                tablePage.stop(true);
            }

            if (null != this.currentTablePage) {
                this.currentTablePage.stop(true);
            }

            logger.info("TABLE STOP ! [DONE]");
        }

    }

    public QuickLocation store(String key, byte[] value, long delayHandleTime, boolean inTransaction) throws Exception {
        if (null != value && !StringUtils.isBlank(key)) {
            this.rotatePage();
            this.counter.incrementAndGet();
            if (logger.isDebugEnabled()) {
                logger.debug("store data key : " + key);
            }

            long offset = this.writeDataFile(this.currentTablePage, value, false);
            long keyOffset = -1L;
            if (inTransaction) {
                keyOffset = this.writeTxFile(this.currentTablePage, key, 0, offset, value.length, delayHandleTime, false, -1L, 0);
            } else {
                keyOffset = this.writeTxFile(this.currentTablePage, key, 1, offset, value.length, delayHandleTime, true, -1L, 0);
            }

            QuickLocation location = new QuickLocation(this.tableName, this.currentTablePage.getFileName(), key, delayHandleTime, offset, value.length, keyOffset, 0);
            if (inTransaction) {
                location.setStatus(0);
            } else {
                location.setStatus(1);
            }

            if (logger.isDebugEnabled()) {
                logger.info(location.toURL());
            }

            return location;
        } else {
            throw new Exception("value is null or key is null . please set it.");
        }
    }

    public void sended(QuickLocation location) throws Exception {
        String key = location.getKey();
        if (logger.isDebugEnabled()) {
            logger.debug("commit data key : " + key);
        }

        this.writeTxFile(this.findTablePage(location), key, location.getStatus(), location.getOffset(), location.getLength(), location.getGmtModify(), false, location.getKeyOffset(), location.getErrCount());
    }

    public void commit(QuickLocation location) throws Exception {
        String key = location.getKey();
        if (logger.isDebugEnabled()) {
            logger.debug("commit data key : " + key);
        }

        this.writeTxFile(this.findTablePage(location), key, 1, location.getOffset(), location.getLength(), location.getGmtModify(), false, location.getKeyOffset(), location.getErrCount());
        location.setStatus(1);
        if (logger.isDebugEnabled()) {
            logger.info(location.toURL());
        }

    }

    public void rollback(QuickLocation location) throws Exception {
        String key = location.getKey();
        if (logger.isDebugEnabled()) {
            logger.debug("rollback data key : " + key);
        }

        this.writeTxFile(this.findTablePage(location), key, 2, location.getOffset(), location.getLength(), location.getGmtModify(), false, location.getKeyOffset(), location.getErrCount());
        location.setStatus(2);
        if (logger.isDebugEnabled()) {
            logger.info(location.toURL());
        }

    }

    public void remove(QuickLocation location) throws Exception {
        String key = location.getKey();
        if (logger.isDebugEnabled()) {
            logger.debug("remove data key : " + key);
        }

        this.writeTxFile(this.findTablePage(location), key, 3, location.getOffset(), location.getLength(), System.currentTimeMillis(), false, location.getKeyOffset(), location.getErrCount());
        location.setStatus(3);
        if (logger.isDebugEnabled()) {
            logger.info(location.toURL());
        }

    }

    public void dead(QuickLocation location) throws Exception {
        String key = location.getKey();
        if (logger.isDebugEnabled()) {
            logger.debug("dead data key : " + key);
        }

        this.writeTxFile(this.findTablePage(location), key, 4, location.getOffset(), location.getLength(), System.currentTimeMillis(), false, location.getKeyOffset(), location.getErrCount());
        location.setStatus(4);
        if (logger.isDebugEnabled()) {
            logger.info(location.toURL());
        }

    }

    private long writeDataFile(TablePage tablePage, byte[] data, boolean sync) throws Exception {
        tablePage.start();
        return tablePage.writeDataFile(data, sync);
    }

    private long writeTxFile(TablePage tablePage, String key, int status, long offset, int length, long delayHandleTime, boolean sync, long keyOffset, int errCount) throws Exception {
        tablePage.start();
        long pos = tablePage.writeTxFile(key, status, offset, length, delayHandleTime, sync, keyOffset, errCount);
        boolean openSync = SystemConfigUtils.getBooleanProperty("event.engine.file.sync", false);
        if (sync && openSync) {
            tablePage.sync();
        }

        return pos;
    }

    private boolean needRotate() {
        long time = System.currentTimeMillis() - this.beginWriteTime;
        return this.counter.get() >= this.maxPageDataSize || this.counter.get() > 0 && time > FILE_KEEP_TIME;
    }

    public void rotatePage() throws Exception {
        if (this.needRotate()) {
            this.lock.lock();

            try {
                if (this.needRotate()) {
                    if (logger.isDebugEnabled()) {
                        logger.warn("page stored message > " + this.maxPageDataSize);
                    }

                    this.changeDate();
                    ++this.pageCounter;
                    this.currentTablePage = this.initCurrentPage();
                    this.counter.set(0);
                    this.beginWriteTime = System.currentTimeMillis();
                }
            } finally {
                this.lock.unlock();
            }
        }

    }

    private TablePage initCurrentPage() throws Exception {
        this.currentFileName = String.valueOf(10000 + this.pageCounter);
        String fileName = this.date + "/" + this.currentFileName;
        return this.initPage(fileName, true);
    }

    private TablePage initPage(String fileName, boolean shouldCreateIt) throws Exception {
        TablePage tempTablePage = new TablePage(this, fileName, this.maxPageDataSize, RECORD_SIZE);
        tempTablePage.start();
        this.tablePageCache.put(fileName, tempTablePage);
        return tempTablePage;
    }

    private TablePage findTablePage(QuickLocation location) throws Exception {
        return StringUtils.equals(this.tableName, location.getTableName()) ? this.findTablePage(location.getFileName()) : null;
    }

    private TablePage findTablePage(String fileName) throws Exception {
        TablePage tablePage = (TablePage)this.tablePageCache.get(fileName);
        if (null == tablePage) {
            File file = new File(this.tableDirName, fileName + ".tx");
            if (!file.exists()) {
                return null;
            }

            this.lock.lock();

            try {
                tablePage = (TablePage)this.tablePageCache.get(fileName);
                if (null == tablePage) {
                    tablePage = this.initPage(fileName, false);
                }
            } finally {
                this.lock.unlock();
            }
        }

        return tablePage;
    }

    public String getRootDataDir() {
        return this.dataDir;
    }

    private String getCurrentDayQueueDir() {
        this.changeDate();
        return this.dataDir + "/" + this.tableName + "/" + this.date + "/";
    }

    private void changeDate() {
        Date tempDate = new Date();
        if (!DateUtils.isSameDay(this.currentDate, tempDate)) {
            this.currentDate = tempDate;
            this.pageCounter = 0;
            this.date = DateUtils.formatShortFormat(this.currentDate);
            String tempDataDir = this.dataDir + "/" + this.tableName + "/" + this.date + "/";
            File file = new File(tempDataDir);
            if (!file.exists()) {
                file.mkdirs();
            }
        }

        if (null == this.date) {
            this.date = DateUtils.formatShortFormat(this.currentDate);
        }

    }

    public String getTableName() {
        return this.tableName;
    }

    public List<QuickRow> getDataList(long gmtBefore) {
        ArrayList kds = new ArrayList(1000);

        try {
            File tableDir = new File(this.tableDirName);
            if (tableDir.exists()) {
                String[] fileNames = tableDir.list();
                if (null != fileNames) {
                    String[] var6 = fileNames;
                    int var7 = fileNames.length;

                    for(int var8 = 0; var8 < var7; ++var8) {
                        String tmpFileName = var6[var8];
                        File toDayDir = new File(this.tableDirName + tmpFileName + "/");
                        if (toDayDir.exists()) {
                            String[] txfiles = toDayDir.list(new FilenameFilter() {
                                public boolean accept(File dir, String name) {
                                    return name.endsWith(".tx");
                                }
                            });
                            String[] var12 = txfiles;
                            int var13 = txfiles.length;

                            for(int var14 = 0; var14 < var13; ++var14) {
                                String txfile = var12[var14];
                                txfile = txfile.substring(0, txfile.length() - 3);
                                TablePage tp = this.findTablePage(tmpFileName + "/" + txfile);
                                if (null != tp && tp.isStarted()) {
                                    Collection<Key> tempKdata = tp.readValideData();
                                    if (null != tempKdata && tempKdata.size() > 0) {
                                        Iterator var18 = tempKdata.iterator();

                                        while(var18.hasNext()) {
                                            Key kd = (Key)var18.next();
                                            QuickRow row = tp.readData(kd);
                                            if (null != row && row.getKey().getGmtModify() < gmtBefore) {
                                                kds.add(row);
                                            }
                                        }
                                    } else if (txfile.indexOf(this.currentFileName) < 0) {
                                        tp.stop(true);
                                        tp.markComplete();
                                        this.tablePageCache.remove(this.date + "/" + txfile);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception var21) {
            logger.error("getDataList has error.", var21);
        }

        return kds;
    }

    public void cleanTableSpace() {
        try {
            logger.debug("clean data begin.... ");
            File tableDir = new File(this.dataDir + "/" + this.tableName + "/");
            if (tableDir.exists()) {
                String[] fileNames = tableDir.list();
                if (null != fileNames) {
                    String[] var3 = fileNames;
                    int var4 = fileNames.length;

                    for(int var5 = 0; var5 < var4; ++var5) {
                        String tmpFileName = var3[var5];
                        this.deleteDoneFile(tmpFileName);
                    }
                }
            }
        } catch (Exception var7) {
            logger.error("", var7);
        }

    }

    private void cleanTable(String path) throws Exception {
        File toDayDir = new File(this.dataDir + "/" + this.tableName + "/" + path + "/");
        if (toDayDir.exists()) {
            logger.info("currentFileName : " + this.currentFileName);
            String[] fileNames = toDayDir.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    if (name.endsWith(".tx")) {
                        return name.indexOf(TableImpl.this.currentFileName) <= -1;
                    } else {
                        return false;
                    }
                }
            });
            if (null != fileNames && fileNames.length > 0) {
                String[] var4 = fileNames;
                int var5 = fileNames.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    String tmpFileName = var4[var6];
                    this.cleanTableHelp(path, tmpFileName);
                }
            }
        }

    }

    private void cleanTableHelp(String path, String tempFileName) throws Exception {
        tempFileName = tempFileName.substring(0, tempFileName.length() - 3);
        TablePage tp = this.findTablePage(path + "/" + tempFileName);
        if (tp.isStarted()) {
            Collection<Key> tempKdata = tp.readValideData();
            if (null != tempKdata && tempKdata.size() != 0) {
                tempKdata.clear();
            } else {
                if (tempFileName.indexOf(this.currentFileName) > -1) {
                    return;
                }

                logger.info("table page : " + this.tableName + "/" + path + "/" + tempFileName + " . has DONE!");
                tp.stop(true);
                tp.markComplete();
                this.tablePageCache.remove(path + "/" + tempFileName);
            }
        }

    }

    private void deleteDoneFile(String path) throws IOException {
        File toDayDir = new File(this.dataDir + "/" + this.tableName + "/" + path + "/");
        if (toDayDir.exists()) {
            String[] fileNames = toDayDir.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".done");
                }
            });
            if (null != fileNames) {
                String[] var4 = fileNames;
                int var5 = fileNames.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    String tmpFileName = var4[var6];
                    logger.info("FILE : " + toDayDir.getAbsolutePath() + "/" + tmpFileName + " . DELETE !");
                    File dFile = new File(toDayDir, tmpFileName);
                    if (dFile.exists()) {
                        this.lock.lock();

                        try {
                            if (dFile.exists()) {
                                FileUtils.forceDelete(dFile);
                            }
                        } catch (Exception var13) {
                            logger.error("del file error. " + toDayDir.getAbsolutePath(), var13);
                        } finally {
                            this.lock.unlock();
                        }
                    }
                }
            }

            fileNames = toDayDir.list();
            if ((null == fileNames || fileNames.length == 0) && toDayDir.getAbsolutePath().indexOf(this.date) < 0 && toDayDir.exists()) {
                logger.info("DIR : " + toDayDir.getAbsolutePath() + " . DELETE ! date = " + this.date);
                FileUtils.forceDelete(toDayDir);
            }
        }

    }

    public boolean requireAdminToken() {
        return this.adminToken.compareAndSet(false, true);
    }

    public void releaseAdminToken() {
        this.adminToken.set(false);
    }
}
