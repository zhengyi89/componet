//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.fstore.impl.journal;

import com.zbjdl.common.utils.event.utils.SystemConfigUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataFileAppender {
    private static final Logger logger = LoggerFactory.getLogger(DataFileAppender.class);
    private final AtomicInteger counter = new AtomicInteger(0);
    private final int MAX_REFRESH_NUMBER = 1000;
    private long lastSyncTime = System.currentTimeMillis();
    private static final long SYNC_TIME = 60000L;
    private int expendLength;
    private long offset = 0L;
    private AtomicBoolean fileIsNew = new AtomicBoolean(true);
    private long fileLength = 0L;
    private RandomAccessFile file;
    private final ReentrantLock lockAppend = new ReentrantLock();
    private List<CountDownLatch> batch = new ArrayList();
    private AtomicBoolean synThreadStarted = new AtomicBoolean(false);

    public DataFileAppender(DataFile dataFile, int expendLength) throws IOException {
        this.file = dataFile.openRandomAccessFile();
        this.fileLength = this.file.length();
    }

    public void stop() throws IOException, InterruptedException {
        this.file.close();
    }

    private long storeItemHelp(byte[] data, boolean sync, long userOffset) throws IOException {
        long oldOffset = this.offset;
        long currentOffset = this.offset;
        if (userOffset < 0L && this.fileIsNew.get()) {
            this.fileIsNew.compareAndSet(true, false);
        }

        if (userOffset > -1L && this.fileIsNew.get()) {
            currentOffset = userOffset;
            oldOffset = userOffset;
        }

        this.file.seek(currentOffset);
        if (this.fileLength < currentOffset + (long)data.length) {
            int tempLength = this.expendLength;
            if (data.length > 2048) {
                tempLength = this.expendLength * data.length / 1024;
            }

            this.file.setLength(this.file.length() + (long)tempLength);
            this.fileLength = this.file.length();
        }

        this.file.write(data, 0, data.length);
        if (userOffset < 0L || !this.fileIsNew.get()) {
            this.offset += (long)data.length;
        }

        boolean openSync = SystemConfigUtils.getBooleanProperty("event.engine.file.sync", false);
        if (sync && openSync) {
            int number = this.counter.incrementAndGet();
            if (number > 1000) {
                this.file.getFD().sync();
                this.counter.set(0);
            } else if (System.currentTimeMillis() - this.lastSyncTime > 60000L) {
                this.file.getFD().sync();
                this.counter.set(0);
                this.lastSyncTime = System.currentTimeMillis();
            }
        }

        return oldOffset;
    }

    public long storeItem(byte[] data, boolean sync, long offset) throws IOException {
        this.lockAppend.lock();

        long var5;
        try {
            var5 = this.storeItemHelp(data, sync, offset);
        } finally {
            this.lockAppend.unlock();
        }

        return var5;
    }

    public void sync(CountDownLatch latch) throws IOException {
        if (this.synThreadStarted.compareAndSet(false, true)) {
            (new Thread(new Runnable() {
                public void run() {
                    try {
                        CountDownLatch latch2 = new CountDownLatch(1);
                        latch2.await(25L, TimeUnit.MILLISECONDS);
                    } catch (Throwable var9) {
                        ;
                    }

                    DataFileAppender.this.lockAppend.lock();

                    try {
                        int number = DataFileAppender.this.counter.get();
                        if (number > 0) {
                            try {
                                DataFileAppender.this.file.getFD().sync();
                            } catch (Exception var8) {
                                DataFileAppender.logger.error("", var8);
                            }

                            DataFileAppender.this.counter.set(0);
                        }

                        Iterator var2 = DataFileAppender.this.batch.iterator();

                        while(var2.hasNext()) {
                            CountDownLatch tempLatch = (CountDownLatch)var2.next();
                            if (null != tempLatch) {
                                tempLatch.countDown();
                            }
                        }
                    } finally {
                        DataFileAppender.this.lockAppend.unlock();
                        DataFileAppender.this.synThreadStarted.set(false);
                    }

                }
            })).start();
        }

        this.lockAppend.lock();

        try {
            this.batch.add(latch);
        } finally {
            this.lockAppend.unlock();
        }

        try {
            latch.await();
        } catch (Exception var5) {
            logger.error("", var5);
        }

    }
}
