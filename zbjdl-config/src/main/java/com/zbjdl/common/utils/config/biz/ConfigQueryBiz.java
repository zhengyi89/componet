package com.zbjdl.common.utils.config.biz;

import com.zbjdl.common.utils.BeanUtils;
import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.config.entity.Config;
import com.zbjdl.common.utils.config.entity.ConfigNamespace;
import com.zbjdl.common.utils.config.entity.ConfigType;
import com.zbjdl.common.utils.config.facade.ConfigQueryFacade;
import com.zbjdl.common.utils.config.param.ConfigDTO;
import com.zbjdl.common.utils.config.param.ConfigNamespaceDTO;
import com.zbjdl.common.utils.config.param.ConfigTypeDTO;
import com.zbjdl.common.utils.config.service.ConfigService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConfigQueryBiz implements ConfigQueryFacade {
    public static String DEFAULT_NAMESPACE = "default";
    private ConfigService configService;

    public ConfigQueryBiz() {
    }

    public Map<String, Map<String, Object>> loadConfig(String namespace, String... types) {
        CheckUtils.valueIsNull(namespace, "namespace");
        Map<String, Map<String, Object>> cfgs = this.configService.loadConfig(namespace, types);
        if (cfgs == null) {
            cfgs = new HashMap();
        }

        if (!DEFAULT_NAMESPACE.equals(namespace)) {
            Map<String, Map<String, Object>> defaultcfgs = this.configService.loadConfig(DEFAULT_NAMESPACE, types);
            if (defaultcfgs == null) {
                return (Map)cfgs;
            } else {
                Iterator var5 = ((Map)cfgs).keySet().iterator();

                while(var5.hasNext()) {
                    String key = (String)var5.next();
                    Map defaultcfg = (Map)defaultcfgs.get(key);
                    Map cfg = (Map)((Map)cfgs).get(key);
                    if (defaultcfg == null) {
                        defaultcfgs.put(key, cfg);
                    } else {
                        defaultcfg.putAll(cfg);
                    }
                }

                return defaultcfgs;
            }
        } else {
            return (Map)cfgs;
        }
    }

    public Map<String, Integer> queryConfigVersion(String namespace, String... types) {
        CheckUtils.notEmpty(namespace, "namespace");
        return this.configService.queryConfigVersion(namespace, types);
    }

    public ConfigDTO queryConfigById(Long id) {
        CheckUtils.isEmpty(id);
        Config config = this.configService.queryConfigById(id);
        if (config == null) {
            return null;
        } else {
            ConfigDTO c = new ConfigDTO();
            BeanUtils.copyProperties(config, c);
            return c;
        }
    }

    public List<ConfigDTO> queryConfig(ConfigDTO config) {
        List<ConfigDTO> result = new ArrayList();
        Config c = new Config();
        BeanUtils.copyProperties(config, c);
        List<Config> list = this.configService.queryConfig(c);
        BeanUtils.copyListProperties(list, result, ConfigDTO.class);
        return result;
    }

    public Boolean configIsOnly(ConfigDTO config) {
        CheckUtils.notEmpty(config, "namespace,type,configKey");
        Config c = new Config();
        BeanUtils.copyProperties(config, c);
        return this.configService.configIsOnly(c);
    }

    public ConfigNamespaceDTO queryConfigNamespace(String namespace) {
        CheckUtils.isEmpty(namespace);
        ConfigNamespaceDTO result = new ConfigNamespaceDTO();
        ConfigNamespace cn = this.configService.queryConfigNamespace(namespace);
        if (cn == null) {
            return null;
        } else {
            BeanUtils.copyProperties(cn, result);
            return result;
        }
    }

    public ConfigTypeDTO queryConfigType(String type) {
        CheckUtils.isEmpty(type);
        ConfigType ct = this.configService.queryConfigType(type);
        if (ct == null) {
            return null;
        } else {
            ConfigTypeDTO result = new ConfigTypeDTO();
            BeanUtils.copyProperties(ct, result);
            return result;
        }
    }

    public List<ConfigDTO> queryConfigByNamespace(String namespace) {
        CheckUtils.isEmpty(namespace);
        List<ConfigDTO> result = new ArrayList();
        List<Config> list = this.configService.queryConfigByNamespace(namespace);
        BeanUtils.copyListProperties(list, result, ConfigDTO.class);
        return result;
    }

    public List<ConfigDTO> queryConfigByType(String type) {
        CheckUtils.isEmpty(type);
        List<ConfigDTO> result = new ArrayList();
        List<Config> list = this.configService.queryConfigByType(type);
        BeanUtils.copyListProperties(list, result, ConfigDTO.class);
        return result;
    }

    public List<ConfigNamespaceDTO> queryConfigNamespace() {
        List<ConfigNamespaceDTO> result = new ArrayList();
        List<ConfigNamespace> list = this.configService.queryConfigNamespace();
        BeanUtils.copyListProperties(list, result, ConfigNamespaceDTO.class);
        return result;
    }

    public List<ConfigTypeDTO> queryConfigType() {
        List<ConfigTypeDTO> result = new ArrayList();
        List<ConfigType> list = this.configService.queryConfigType();
        BeanUtils.copyListProperties(list, result, ConfigTypeDTO.class);
        return result;
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }
}
