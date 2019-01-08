//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.fstore.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class DiskBenchmark {
    boolean verbose;
    int bs = 4096;
    long size = 524288000L;
    long sampleInterval = 10000L;

    public DiskBenchmark() {
    }

    public static void main(String[] args) {
        DiskBenchmark benchmark = new DiskBenchmark();
        ArrayList<String> files = new ArrayList();
        if (args.length == 0) {
            files.add("disk-benchmark.dat");
        } else {
            files.addAll(Arrays.asList(args));
        }

        Iterator var3 = files.iterator();

        while(var3.hasNext()) {
            String f = (String)var3.next();

            try {
                File file = new File(f);
                if (file.exists()) {
                    System.out.println("File " + file + " allready exists, will not benchmark.");
                } else {
                    System.out.println("Benchmarking: " + file.getCanonicalPath());
                    DiskBenchmark.Report report = benchmark.benchmark(file);
                    file.delete();
                    System.out.println(report.toString());
                }
            } catch (Throwable var7) {
                if (benchmark.verbose) {
                    System.out.println("ERROR:");
                    var7.printStackTrace(System.out);
                } else {
                    System.out.println("ERROR: " + var7);
                }
            }
        }

    }

    public DiskBenchmark.Report benchmark(File file) throws IOException {
        DiskBenchmark.Report rc = new DiskBenchmark.Report();
        byte[] data = new byte[this.bs];

        for(int i = 0; i < data.length; ++i) {
            data[i] = (byte)(97 + i % 26);
        }

        rc.size = data.length;
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.setLength(this.size);
        long start = System.currentTimeMillis();
        long now = System.currentTimeMillis();

        int ioCount;
        long i;
        for(ioCount = 0; now - start <= this.sampleInterval; raf.getFD().sync()) {
            raf.seek(0L);

            for(i = 0L; i + (long)data.length < this.size; i += (long)data.length) {
                raf.write(data);
                ++ioCount;
                now = System.currentTimeMillis();
                if (now - start > this.sampleInterval) {
                    break;
                }
            }
        }

        raf.getFD().sync();
        raf.close();
        now = System.currentTimeMillis();
        rc.size = data.length;
        rc.writes = ioCount;
        rc.writeDuration = now - start;
        raf = new RandomAccessFile(file, "rw");
        start = System.currentTimeMillis();
        now = System.currentTimeMillis();
        ioCount = 0;

        while(now - start <= this.sampleInterval) {
            for(i = 0L; i + (long)data.length < this.size; i += (long)data.length) {
                raf.seek(i);
                raf.write(data);
                raf.getFD().sync();
                ++ioCount;
                now = System.currentTimeMillis();
                if (now - start > this.sampleInterval) {
                    break;
                }
            }
        }

        raf.close();
        now = System.currentTimeMillis();
        rc.syncWrites = ioCount;
        rc.syncWriteDuration = now - start;
        raf = new RandomAccessFile(file, "rw");
        start = System.currentTimeMillis();
        now = System.currentTimeMillis();
        ioCount = 0;

        while(now - start <= this.sampleInterval) {
            raf.seek(0L);

            for(i = 0L; i + (long)data.length < this.size; i += (long)data.length) {
                raf.seek(i);
                raf.readFully(data);
                ++ioCount;
                now = System.currentTimeMillis();
                if (now - start > this.sampleInterval) {
                    break;
                }
            }
        }

        raf.close();
        rc.reads = ioCount;
        rc.readDuration = now - start;
        return rc;
    }

    public boolean isVerbose() {
        return this.verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public int getBs() {
        return this.bs;
    }

    public void setBs(int bs) {
        this.bs = bs;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getSampleInterval() {
        return this.sampleInterval;
    }

    public void setSampleInterval(long sampleInterval) {
        this.sampleInterval = sampleInterval;
    }

    public static class Report {
        public int size;
        public int writes;
        public long writeDuration;
        public int syncWrites;
        public long syncWriteDuration;
        public int reads;
        public long readDuration;

        public Report() {
        }

        public String toString() {
            return "Writes: \n  " + this.writes + " writes of size " + this.size + " written in " + (double)this.writeDuration / 1000.0D + " seconds.\n  " + this.getWriteRate() + " writes/second.\n  " + this.getWriteSizeRate() + " megs/second.\n\nSync Writes: \n  " + this.syncWrites + " writes of size " + this.size + " written in " + (double)this.syncWriteDuration / 1000.0D + " seconds.\n  " + this.getSyncWriteRate() + " writes/second.\n  " + this.getSyncWriteSizeRate() + " megs/second.\n\nReads: \n  " + this.reads + " reads of size " + this.size + " read in " + (double)this.readDuration / 1000.0D + " seconds.\n  " + this.getReadRate() + " writes/second.\n  " + this.getReadSizeRate() + " megs/second.\n\n";
        }

        private float getWriteSizeRate() {
            float rc = (float)this.writes;
            rc *= (float)this.size;
            rc /= 1048576.0F;
            rc = (float)((double)rc / ((double)this.writeDuration / 1000.0D));
            return rc;
        }

        private float getWriteRate() {
            float rc = (float)this.writes;
            rc = (float)((double)rc / ((double)this.writeDuration / 1000.0D));
            return rc;
        }

        private float getSyncWriteSizeRate() {
            float rc = (float)this.syncWrites;
            rc *= (float)this.size;
            rc /= 1048576.0F;
            rc = (float)((double)rc / ((double)this.syncWriteDuration / 1000.0D));
            return rc;
        }

        private float getSyncWriteRate() {
            float rc = (float)this.syncWrites;
            rc = (float)((double)rc / ((double)this.syncWriteDuration / 1000.0D));
            return rc;
        }

        private float getReadSizeRate() {
            float rc = (float)this.reads;
            rc *= (float)this.size;
            rc /= 1048576.0F;
            rc = (float)((double)rc / ((double)this.readDuration / 1000.0D));
            return rc;
        }

        private float getReadRate() {
            float rc = (float)this.reads;
            rc = (float)((double)rc / ((double)this.readDuration / 1000.0D));
            return rc;
        }

        public int getSize() {
            return this.size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getWrites() {
            return this.writes;
        }

        public void setWrites(int writes) {
            this.writes = writes;
        }

        public long getWriteDuration() {
            return this.writeDuration;
        }

        public void setWriteDuration(long writeDuration) {
            this.writeDuration = writeDuration;
        }

        public int getSyncWrites() {
            return this.syncWrites;
        }

        public void setSyncWrites(int syncWrites) {
            this.syncWrites = syncWrites;
        }

        public long getSyncWriteDuration() {
            return this.syncWriteDuration;
        }

        public void setSyncWriteDuration(long syncWriteDuration) {
            this.syncWriteDuration = syncWriteDuration;
        }

        public int getReads() {
            return this.reads;
        }

        public void setReads(int reads) {
            this.reads = reads;
        }

        public long getReadDuration() {
            return this.readDuration;
        }

        public void setReadDuration(long readDuration) {
            this.readDuration = readDuration;
        }
    }
}
