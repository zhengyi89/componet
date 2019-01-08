package com.zbjdl.common.utils.config.mock;

import com.zbjdl.common.utils.config.ConfigParam;
import com.zbjdl.common.utils.config.ConfigParamGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockConfigParamGroupImpl implements ConfigParamGroup {
    private Map<String, ConfigParam> configs = new HashMap();

    public MockConfigParamGroupImpl() {
    }

    public void addConfig(String key, ConfigParam config) {
        this.configs.put(key, config);
    }

    public ConfigParam getConfig(String key) {
        return (ConfigParam)this.configs.get(key);
    }

    public List<ConfigParam> values() {
        return new ArrayList(this.configs.values());
    }

    public List<String> keys() {
        return new ArrayList(this.configs.keySet());
    }
}
