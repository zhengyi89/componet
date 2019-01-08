//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.client.model;

public enum CodeEnum {
    HTTP(1),
    HESSIEN(2);

    private int code;

    private CodeEnum(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
