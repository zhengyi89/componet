package com.zbjdl.common.utils.config.facade;

import com.zbjdl.common.utils.config.param.ConfigDTO;
import com.zbjdl.common.utils.config.param.ConfigNamespaceDTO;
import com.zbjdl.common.utils.config.param.ConfigTypeDTO;

public interface ConfigManagementFacade {
    Long createConfig(ConfigDTO var1);

    void changeConfig(ConfigDTO var1);

    void deleteConfig(Long var1);

    void saveOrUpdateConfigNamespace(ConfigNamespaceDTO var1);

    void saveOrUpdateConfigType(ConfigTypeDTO var1);

    void deleteConfigNamespace(String var1);

    void deleteConfigType(String var1);
}
