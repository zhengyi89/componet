//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.fstore;

import java.util.Collection;

public interface TableManager {
    Table findTableForWrite();

    void recoverData(DataRecoverListener var1);

    void cleanTable();

    Table findTable(String var1);

    /** @deprecated */
    @Deprecated
    Table findTable(String var1, int var2, int var3);

    Collection<String> tableNames();
}
