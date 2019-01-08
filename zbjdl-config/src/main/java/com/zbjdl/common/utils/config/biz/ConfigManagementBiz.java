package com.zbjdl.common.utils.config.biz;

import com.zbjdl.common.utils.BeanUtils;
import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.config.entity.Config;
import com.zbjdl.common.utils.config.entity.ConfigNamespace;
import com.zbjdl.common.utils.config.entity.ConfigType;
import com.zbjdl.common.utils.config.facade.ConfigManagementFacade;
import com.zbjdl.common.utils.config.param.ConfigDTO;
import com.zbjdl.common.utils.config.param.ConfigNamespaceDTO;
import com.zbjdl.common.utils.config.param.ConfigTypeDTO;
import com.zbjdl.common.utils.config.service.ConfigService;

public class ConfigManagementBiz implements ConfigManagementFacade {
    private ConfigService configService;

    public ConfigManagementBiz() {
    }

    public Long createConfig(ConfigDTO config) {
        CheckUtils.notEmpty(config, "namespace,type,configKey,valueDataType,valueType,value");
        Config c = new Config();
        BeanUtils.copyProperties(config, c);
        return this.configService.createConfig(c);
    }

    public void changeConfig(ConfigDTO config) {
        CheckUtils.notEmpty(config, "id,namespace,type,configKey,valueDataType,valueType,value");
        Config c = new Config();
        BeanUtils.copyProperties(config, c);
        this.configService.changeConfig(c);
    }

    public void deleteConfig(Long id) {
        CheckUtils.isEmpty(id);
        this.configService.deleteConfig(id);
    }

    public void saveOrUpdateConfigNamespace(ConfigNamespaceDTO configNamespace) {
        CheckUtils.notEmpty(configNamespace, "code");
        ConfigNamespace cn = new ConfigNamespace();
        BeanUtils.copyProperties(configNamespace, cn);
        this.configService.saveOrUpdateConfigNamespace(cn);
    }

    public void saveOrUpdateConfigType(ConfigTypeDTO configType) {
        CheckUtils.notEmpty(configType, "code");
        ConfigType ct = new ConfigType();
        BeanUtils.copyProperties(configType, ct);
        this.configService.saveOrUpdateConfigType(ct);
    }

    public void deleteConfigNamespace(String namespace) {
        CheckUtils.isEmpty(namespace);
        this.configService.deleteConfigNamespace(namespace);
    }

    public void deleteConfigType(String type) {
        CheckUtils.isEmpty(type);
        this.configService.deleteConfigType(type);
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }
}
