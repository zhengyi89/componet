//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.fstore.impl.journal;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class DataFileAppenderNio {
    private final int INCREMENT_SIZE = 2097152;
    private FileChannel channel;

    public DataFileAppenderNio(DataFile dataFile) throws IOException {
        this.channel = dataFile.openRandomAccessFile().getChannel();
    }

    public void stop() throws IOException {
        this.channel.close();
    }

    public long storeItem(byte[] data, boolean sync) throws IOException {
        if (this.channel.size() < this.channel.position() + (long)data.length) {
            long oldPos = this.channel.position();
            this.channel.truncate(this.channel.size() + 2097152L);
            this.channel.position(oldPos);
        }

        ByteBuffer buf = ByteBuffer.wrap(data);
        buf.position(0);
        buf.limit(0 + data.length);
        this.channel.write(buf);
        if (sync) {
            this.channel.force(true);
        }

        return this.channel.position();
    }
}
