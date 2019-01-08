//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.cache.config;

import java.io.Serializable;

public class CachedResult implements Serializable {
    private static final long serialVersionUID = 6243274087256160277L;
    private Object result;
    private Throwable exception;

    public CachedResult(Object _result, Throwable _exception) {
        this.result = _result;
        this.exception = _exception;
    }

    public boolean hasException() {
        return this.exception != null;
    }

    public Object getResult() {
        return this.result;
    }

    public Throwable getException() {
        return this.exception;
    }

    public Object result() throws Throwable {
        if (this.exception != null) {
            throw this.exception;
        } else {
            return this.result;
        }
    }
}
