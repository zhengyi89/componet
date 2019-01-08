//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.fstore;

public class TableManagerFactory {
    private static TableManager globalTableManager;

    public TableManagerFactory() {
    }

    public static void registerTableManager(TableManager tableManager) {
        globalTableManager = tableManager;
    }

    public static TableManager findTableManager() {
        return globalTableManager;
    }
}
