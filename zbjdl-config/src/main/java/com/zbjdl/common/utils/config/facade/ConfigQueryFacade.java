package com.zbjdl.common.utils.config.facade;

import com.zbjdl.common.utils.config.param.ConfigDTO;
import com.zbjdl.common.utils.config.param.ConfigNamespaceDTO;
import com.zbjdl.common.utils.config.param.ConfigTypeDTO;

import java.util.List;
import java.util.Map;

public interface ConfigQueryFacade {
    Map<String, Map<String, Object>> loadConfig(String var1, String... var2);

    Map<String, Integer> queryConfigVersion(String var1, String... var2);

    ConfigDTO queryConfigById(Long var1);

    List<ConfigDTO> queryConfig(ConfigDTO var1);

    Boolean configIsOnly(ConfigDTO var1);

    ConfigNamespaceDTO queryConfigNamespace(String var1);

    ConfigTypeDTO queryConfigType(String var1);

    List<ConfigDTO> queryConfigByNamespace(String var1);

    List<ConfigDTO> queryConfigByType(String var1);

    List<ConfigNamespaceDTO> queryConfigNamespace();

    List<ConfigTypeDTO> queryConfigType();
}
