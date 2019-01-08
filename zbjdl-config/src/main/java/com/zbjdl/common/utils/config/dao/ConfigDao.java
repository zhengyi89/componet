package com.zbjdl.common.utils.config.dao;

import com.zbjdl.common.persistence.GenericDao;
import com.zbjdl.common.utils.config.entity.Config;

import java.util.List;

public interface ConfigDao extends GenericDao<Config> {
    Config queryConfigByKey(String var1, String var2, String var3);

    List<Config> queryConfigByNamespace(String var1);

    List<Config> queryConfigByType(String var1);

    List<Config> queryConfigByTypes(String var1, String... var2);

    List<Config> queryConfig(Config var1);
}
