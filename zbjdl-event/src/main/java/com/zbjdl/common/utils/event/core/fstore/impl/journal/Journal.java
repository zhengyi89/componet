//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.fstore.impl.journal;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Journal {
    private static final Logger LOG = LoggerFactory.getLogger(Journal.class);
    protected File directory;
    protected String fileName;
    protected int initLength;
    protected int expendLength;
    protected DataFileAppender appender;
    protected DataFileAccessor reader;
    protected boolean started = false;

    public Journal() {
    }

    public synchronized void start() throws IOException {
        if (!this.started) {
            long start = System.currentTimeMillis();
            DataFile dataFile = this.getCurrentWriteFile();
            if (!dataFile.getFile().getParentFile().exists()) {
                dataFile.getFile().getParentFile().mkdir();
            }

            this.appender = new DataFileAppender(dataFile, this.expendLength);
            this.reader = new DataFileAccessor(dataFile);
            long end = System.currentTimeMillis();
            LOG.trace("Startup took: " + (end - start) + " ms");
            this.started = true;
        }
    }

    public synchronized void stop() throws IOException, InterruptedException {
        if (this.started) {
            this.started = false;
            this.appender.stop();
            this.reader.stop();
        }
    }

    synchronized DataFile getCurrentWriteFile() throws IOException {
        return new DataFile(this.getFile(), this.initLength);
    }

    private File getFile() {
        File file = new File(this.directory, this.fileName);
        return file;
    }

    public boolean checkFileExist() {
        return this.getFile().exists();
    }

    public synchronized long write(byte[] data, boolean sync) throws IOException, IllegalStateException {
        this.start();
        return this.appender.storeItem(data, sync, -1L);
    }

    public synchronized long writeOrUpdate(byte[] data, boolean sync, long offset) throws IOException, IllegalStateException {
        this.start();
        return this.appender.storeItem(data, sync, offset);
    }

    public void sync(CountDownLatch latch) throws IOException {
        this.start();
        this.appender.sync(latch);
    }

    public long fileLength() throws IOException {
        this.start();
        return null == this.reader ? -1L : this.reader.fileLength();
    }

    public void readFully(long offset, byte[] data) throws IOException {
        this.start();
        this.reader.readFully(offset, data);
    }

    public int read(long offset, byte[] data) throws IOException {
        this.start();
        return this.reader.read(offset, data);
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setInitLength(int initLength) {
        this.initLength = initLength;
    }

    public void setExpendLength(int expendLength) {
        this.expendLength = expendLength;
    }
}
