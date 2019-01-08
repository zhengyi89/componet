//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.fstore.impl.journal;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DataFile {
    protected final File file;
    protected int length;

    DataFile(File file, int fileInitSize) {
        this.file = file;
        this.length = (int)(file.exists() ? file.length() : (long)fileInitSize);
    }

    public File getFile() {
        return this.file;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public synchronized RandomAccessFile openRandomAccessFile() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(this.file, "rw");
        raf.setLength((long)this.length);
        return raf;
    }
}
