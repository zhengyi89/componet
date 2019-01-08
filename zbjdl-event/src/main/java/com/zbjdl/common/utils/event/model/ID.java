package com.zbjdl.common.utils.event.model;

public abstract class ID {
    protected static final int UNKNOW_BIZ_CODE = 99;

    public ID() {
    }

    public abstract String getID();

    public abstract int getBizCode();
}
