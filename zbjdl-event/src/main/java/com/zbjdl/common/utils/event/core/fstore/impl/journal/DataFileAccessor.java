//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.fstore.impl.journal;

import java.io.IOException;
import java.io.RandomAccessFile;

final class DataFileAccessor {
    private final DataFile dataFile;
    private RandomAccessFile file;
    private boolean disposed = true;

    public DataFileAccessor(DataFile dataFile) throws IOException {
        this.dataFile = dataFile;
        this.start();
    }

    public DataFile getDataFile() {
        return this.dataFile;
    }

    public void start() throws IOException {
        if (this.disposed) {
            this.disposed = false;
            this.file = this.dataFile.openRandomAccessFile();
        }
    }

    public void stop() throws IOException {
        if (!this.disposed) {
            this.disposed = true;
            this.file.close();
        }
    }

    public void readFully(long offset, byte[] data) throws IOException {
        this.start();
        this.file.seek(offset);
        this.file.readFully(data);
    }

    public int read(long offset, byte[] data) throws IOException {
        this.start();
        this.file.seek(offset);
        return this.file.read(data);
    }

    public long fileLength() throws IOException {
        this.start();
        return this.file.length();
    }
}
