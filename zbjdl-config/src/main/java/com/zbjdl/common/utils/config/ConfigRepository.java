package com.zbjdl.common.utils.config;

public interface ConfigRepository {
    void loadConfig(String... var1);

    void loadAllConfig();

    ConfigParam getConfig(String var1, String var2);

    ConfigParamGroup getConfigGroup(String var1);

    void addListener(ConfigNotifyListener var1);
}
