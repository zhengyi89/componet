//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.fstore.impl;

import com.zbjdl.common.utils.event.core.fstore.QuickLocation;
import com.zbjdl.common.utils.event.core.fstore.QuickRow;
import com.zbjdl.common.utils.event.core.fstore.impl.journal.Journal;
import com.zbjdl.common.utils.event.utils.DateUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TablePage {
    private static final Logger logger = LoggerFactory.getLogger(TablePage.class);
    private String fileName;
    private int maxPageDataSize;
    private int recordSize;
    private Journal dataJournal;
    private Journal txJournal;
    private AtomicInteger counter = new AtomicInteger(0);
    private TableImpl table;
    private AtomicBoolean started = new AtomicBoolean();
    private static final int DATA_LENGTH_MIN = 2097152;
    private static final int DATA_LENGTH_MAX = 10485760;
    public static int KEY_LENGTH = 100;
    ArrayList<QuickRow> EMPTY_VALIDE_DATA = new ArrayList();
    Collection<TablePage.Key> EMPTY_VALIDE_KEY = new ArrayList();

    public TablePage(TableImpl table, String fileName, int maxPageDataSize, int recordSize) {
        this.table = table;
        this.fileName = fileName;
        if (maxPageDataSize < 100) {
            maxPageDataSize = 100;
        }

        this.maxPageDataSize = maxPageDataSize;
        if (recordSize < 100) {
            recordSize = 256;
        }

        this.recordSize = recordSize;
    }

    public String getFileName() {
        return this.fileName;
    }

    public boolean isStarted() {
        return this.started.get();
    }

    public void start() throws IOException {
        if (this.started.compareAndSet(false, true)) {
            if (logger.isDebugEnabled()) {
                logger.info("table page : " + this.fileName + " is starting...");
            }

            int dataLength = this.recordSize * this.maxPageDataSize / 5;
            if (dataLength < 2097152) {
                dataLength = 2097152;
            }

            if (dataLength > 10485760) {
                dataLength = 10485760;
            }

            this.dataJournal = this.createJournal(this.table.getRootDataDir(), this.table.getTableName(), this.fileName + ".data", dataLength, dataLength);
            int txLength = KEY_LENGTH * this.maxPageDataSize / 5;
            this.txJournal = this.createJournal(this.table.getRootDataDir(), this.table.getTableName(), this.fileName + ".tx", txLength, KEY_LENGTH * 1024);
            this.dataJournal.start();
            this.txJournal.start();
            if (logger.isDebugEnabled()) {
                logger.info("table page : " + this.fileName + " is start success!");
            }
        }

    }

    public boolean stop() throws IOException, InterruptedException {
        return this.stop(false);
    }

    public boolean stop(boolean isForace) throws IOException, InterruptedException {
        if ((this.counter.get() == 0 || isForace) && this.started.compareAndSet(true, false)) {
            if (logger.isDebugEnabled()) {
                logger.info("table page : " + this.fileName + " is stoping...");
            }

            this.dataJournal.stop();
            this.txJournal.stop();
            if (logger.isDebugEnabled()) {
                logger.info("table page : " + this.fileName + " is stop success!");
            }

            return true;
        } else {
            return false;
        }
    }

    public void increment() {
        this.counter.incrementAndGet();
    }

    public void decrement() {
        this.counter.decrementAndGet();
    }

    public void sync() throws IOException {
        this.txJournal.sync(new CountDownLatch(1));
        this.dataJournal.sync(new CountDownLatch(1));
    }

    public long writeDataFile(byte[] value, boolean sync) throws IOException, IllegalStateException {
        return this.dataJournal.write(value, sync);
    }

    public long writeTxFile(String key, int status, long offset, int length, long delayHandleTime, boolean sync, long keyOffset, int errCount) throws IOException, IllegalStateException {
        StringBuilder builder = new StringBuilder(100);
        builder.append(key).append(",").append(status).append(",").append(offset).append(",").append(length).append(",").append(delayHandleTime).append(",").append(errCount);
        byte[] data = new byte[KEY_LENGTH];
        byte[] bt = builder.toString().getBytes();
        if (bt.length > KEY_LENGTH) {
            throw new RuntimeException("Key is too long , should less 100 byte . key = " + builder.toString());
        } else {
            System.arraycopy(bt, 0, data, 0, bt.length);
            long pos = this.txJournal.writeOrUpdate(data, sync, keyOffset);
            return pos;
        }
    }

    private Journal createJournal(String dataDir, String tableName, String tempFileName, int initLength, int expendLength) {
        Journal manager = new Journal();
        manager.setDirectory(new File(dataDir + "/" + tableName));
        manager.setFileName(tempFileName);
        manager.setInitLength(initLength);
        return manager;
    }

    public Collection<TablePage.Key> readValideData() throws IOException {
        int offset = 0;
        long length = this.txJournal.fileLength();
        if (length < 100L) {
            return this.EMPTY_VALIDE_KEY;
        } else {
            byte[] content = new byte[(int)length];
            this.txJournal.readFully(0L, content);
            Map<String, TablePage.Key> cache = new LinkedHashMap();
            String[] sKeys = null;
            String sLine = null;
            byte[] line = null;
            TablePage.Key key = null;

            int status;
            while(offset + KEY_LENGTH < content.length) {
                byte[] line1 = new byte[KEY_LENGTH];
                System.arraycopy(content, offset, line1, 0, KEY_LENGTH);
                offset += KEY_LENGTH;
                sLine = new String(line1);
                if (StringUtils.isBlank(sLine)) {
                    break;
                }

                sKeys = StringUtils.split(sLine, ",");
                if (null == sKeys || sKeys.length != 6) {
                    break;
                }

                if (cache.containsKey(sKeys[0])) {
                    key = (TablePage.Key)cache.get(sKeys[0]);
                    key.merge(NumberUtils.toInt(sKeys[1]), NumberUtils.toLong(sKeys[2]), NumberUtils.toInt(sKeys[3]), NumberUtils.toLong(sKeys[4]), NumberUtils.toInt(sKeys[5]));
                } else {
                    status = NumberUtils.toInt(sKeys[1]);
                    if (!TablePage.Key.hasDone(status)) {
                        key = new TablePage.Key(sKeys[0], status, NumberUtils.toLong(sKeys[2]), NumberUtils.toInt(sKeys[3]), NumberUtils.toLong(sKeys[4]), (long)(offset - KEY_LENGTH), NumberUtils.toInt(sKeys[5]));
                        cache.put(sKeys[0], key);
                        if (logger.isDebugEnabled()) {
                            logger.debug("row is new. key = " + sKeys[0]);
                        }
                    }
                }
            }

            line = null;
            sLine = null;
            byte[] content1 = null;

            for(status = 0; status < sKeys.length; ++status) {
                sKeys[status] = null;
            }

            sKeys = null;
            Collection<TablePage.Key> keys = new ArrayList();
            Collection<TablePage.Key> temps = cache.values();
            Iterator var12 = temps.iterator();

            while(var12.hasNext()) {
                TablePage.Key temp = (TablePage.Key)var12.next();
                if (!temp.hasDone()) {
                    keys.add(temp);
                }
            }

            cache.clear();
            if (logger.isDebugEnabled()) {
                logger.info("可用事件汇总:tableName=" + this.table.getTableName() + " 文件名=" + this.fileName + " 数量=" + keys.size() + " 详情=" + keys);
            }

            return keys;
        }
    }

    public QuickRow readData(TablePage.Key keyObj) {
        try {
            long voffset = keyObj.offset;
            long vlength = (long)keyObj.length;
            if (vlength <= 0L) {
                logger.error("read key : " + keyObj.getKey() + " | value length is 0");
                return null;
            } else {
                byte[] vv = new byte[(int)vlength];
                this.dataJournal.read(voffset, vv);
                QuickLocation location = new QuickLocation(this.table.getTableName(), this.fileName, keyObj.key, keyObj.gmtModify, keyObj.offset, keyObj.length, keyObj.keyOffset, keyObj.errCount);
                location.setStatus(keyObj.status);
                return new QuickRow(location, vv);
            }
        } catch (Exception var8) {
            logger.error("read key : " + keyObj.getKey(), var8);
            return null;
        }
    }

    public void markComplete() throws IOException {
        String dataDir = this.table.getRootDataDir() + "/" + this.table.getTableName();
        File data = new File(dataDir, this.fileName + ".data");
        if (data.exists()) {
            boolean r = data.renameTo(new File(dataDir, this.fileName + ".data.done"));
            if (logger.isDebugEnabled()) {
                logger.warn(dataDir + this.fileName + ".data.done rename " + r);
            }
        }

        File tx = new File(dataDir, this.fileName + ".tx");
        tx.renameTo(new File(dataDir, this.fileName + ".tx.done"));
    }

    public void markModified() {
        String dataDir = this.table.getRootDataDir() + "/" + this.table.getTableName();
        File tx = new File(dataDir, this.fileName + ".tx");
        tx.setLastModified(System.currentTimeMillis());
    }

    public static class Key {
        private String key;
        private int status;
        private long offset;
        private int length;
        private long gmtModify;
        private long keyOffset;
        private int errCount;

        public Key(String key, int status, long offset, int length, long gmtModify, long keyOffset, int errCount) {
            this.key = key;
            this.status = status;
            this.offset = offset;
            this.length = length;
            this.gmtModify = gmtModify;
            this.keyOffset = keyOffset;
            this.errCount = errCount;
        }

        public void merge(int tStatus, long offset, int length, long gmtModify, int errCount) {
            if (tStatus == 0) {
                this.offset = offset;
                this.length = length;
                if (this.status == 0) {
                    this.gmtModify = gmtModify;
                }
            } else if (tStatus == 1) {
                if (offset != -1L) {
                    this.offset = offset;
                }

                if (length != -1) {
                    this.length = length;
                }

                if (this.status == 0) {
                    this.status = 1;
                }

                this.gmtModify = gmtModify;
                this.errCount = errCount;
            } else if (tStatus == 2) {
                this.status = 2;
                this.gmtModify = gmtModify;
            } else if (tStatus == 3) {
                this.status = 3;
                this.gmtModify = gmtModify;
            } else if (tStatus == 4) {
                this.status = 4;
                this.gmtModify = gmtModify;
            }

        }

        public static boolean hasDone(int status) {
            if (status == 2) {
                return true;
            } else if (status == 3) {
                return true;
            } else {
                return status == 4;
            }
        }

        public boolean hasDone() {
            return hasDone(this.status);
        }

        public String getKey() {
            return this.key;
        }

        public int getStatus() {
            return this.status;
        }

        public long getOffset() {
            return this.offset;
        }

        public long getLength() {
            return (long)this.length;
        }

        public long getGmtModify() {
            return this.gmtModify;
        }

        public long getKeyOffset() {
            return this.keyOffset;
        }

        public void setOffset(long offset) {
            this.offset = offset;
        }

        public int getErrCount() {
            return this.errCount;
        }

        public String toString() {
            try {
                String str = ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
                str = str + " gmtModify=" + DateUtils.formatByLong(this.gmtModify, DateUtils.newFormat);
                return str;
            } catch (Exception var2) {
                return super.toString();
            }
        }
    }
}
