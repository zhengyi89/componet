package com.zbjdl.common.utils.config.mock;

import com.zbjdl.common.utils.config.ConfigNotifyListener;
import com.zbjdl.common.utils.config.ConfigParam;
import com.zbjdl.common.utils.config.ConfigParamGroup;
import com.zbjdl.common.utils.config.ConfigRepository;

public class NoneConfigRepository implements ConfigRepository {
    public NoneConfigRepository() {
    }

    public void loadConfig(String... types) {
    }

    public void loadAllConfig() {
    }

    public ConfigParam getConfig(String configType, String configKey) {
        return null;
    }

    public ConfigParamGroup getConfigGroup(String configType) {
        return null;
    }

    public void addListener(ConfigNotifyListener listener) {
    }
}
