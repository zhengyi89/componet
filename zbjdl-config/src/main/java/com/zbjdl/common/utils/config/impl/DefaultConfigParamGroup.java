package com.zbjdl.common.utils.config.impl;

import com.zbjdl.common.utils.config.ConfigParam;
import com.zbjdl.common.utils.config.ConfigParamGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class DefaultConfigParamGroup implements ConfigParamGroup {
    private final Lock readLock;
    private final Map<String, Map<String, Object>> configParams;
    private final String configType;

    public DefaultConfigParamGroup(String type, Lock lock, Map<String, Map<String, Object>> params) {
        this.configType = type;
        this.readLock = lock;
        this.configParams = params;
    }

    public ConfigParam<?> getConfig(String configKey) {
        return new DefaultConfigParam(this.configType, configKey, this.readLock, this.configParams);
    }

    public List<ConfigParam> values() {
        List<ConfigParam> result = new ArrayList();
        Map<String, Object> params = (Map)this.configParams.get(this.configType);
        if (params != null) {
            Iterator var3 = params.keySet().iterator();

            while(var3.hasNext()) {
                String key = (String)var3.next();
                ConfigParam param = new DefaultConfigParam(key, this.configType, this.readLock, this.configParams);
                result.add(param);
            }
        }

        return result;
    }

    public List<String> keys() {
        List<String> result = new ArrayList();
        Map<String, Object> params = (Map)this.configParams.get(this.configType);
        if (params != null) {
            result.addAll(params.keySet());
        }

        return result;
    }
}
