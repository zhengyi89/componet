package com.zbjdl.common.utils.config.service;

import com.zbjdl.common.utils.config.entity.Config;
import com.zbjdl.common.utils.config.entity.ConfigNamespace;
import com.zbjdl.common.utils.config.entity.ConfigType;

import java.util.List;
import java.util.Map;

public interface ConfigService {
    void saveOrUpdateConfig(Config var1);

    Long createConfig(Config var1);

    Map<String, Map<String, Object>> loadConfig(String var1, String... var2);

    Map<String, Integer> queryConfigVersion(String var1, String... var2);

    Config queryConfigById(Long var1);

    void changeConfig(Config var1);

    void deleteConfig(Long var1);

    List<Config> queryConfig(Config var1);

    Boolean configIsOnly(Config var1);

    ConfigNamespace queryConfigNamespace(String var1);

    ConfigType queryConfigType(String var1);

    void saveOrUpdateConfigNamespace(ConfigNamespace var1);

    void saveOrUpdateConfigType(ConfigType var1);

    List<Config> queryConfigByNamespace(String var1);

    List<Config> queryConfigByType(String var1);

    void deleteConfigNamespace(String var1);

    void deleteConfigType(String var1);

    List<ConfigNamespace> queryConfigNamespace();

    List<ConfigType> queryConfigType();
}
