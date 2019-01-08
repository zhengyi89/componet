package com.zbjdl.common.utils.config.impl;

import com.zbjdl.common.utils.config.ConfigParam;

import java.util.Map;
import java.util.concurrent.locks.Lock;

public class DefaultConfigParam<T> implements ConfigParam<T> {
    private final Lock readLock;
    private final Map<String, Map<String, Object>> configParams;
    private final String configType;
    private final String configKey;

    public DefaultConfigParam(String type, String key, Lock lock, Map<String, Map<String, Object>> params) {
        this.configType = type;
        this.configKey = key;
        this.readLock = lock;
        this.configParams = params;
    }

    public T getValue() {
        this.readLock.lock();

        Map params;
        try {
            params = (Map)this.configParams.get(this.configType);
        } finally {
            this.readLock.unlock();
        }

        return (T) (params != null ? params.get(this.configKey) : null);
    }
}
