//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.fstore;

import java.util.List;

public interface Table {
    QuickLocation store(String var1, byte[] var2, long var3, boolean var5) throws Exception;

    void sended(QuickLocation var1) throws Exception;

    void commit(QuickLocation var1) throws Exception;

    void rollback(QuickLocation var1) throws Exception;

    void remove(QuickLocation var1) throws Exception;

    void rotatePage() throws Exception;

    void cleanTableSpace();

    boolean requireAdminToken();

    void releaseAdminToken();

    List<QuickRow> getDataList(long var1);

    void dead(QuickLocation var1) throws Exception;
}
