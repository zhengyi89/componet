package com.zbjdl.common.utils.config.dao;

import com.zbjdl.common.persistence.GenericDao;
import com.zbjdl.common.utils.config.entity.ConfigNamespace;

public interface ConfigNamespaceDao extends GenericDao<ConfigNamespace> {
    ConfigNamespace queryByCode(String var1);
}
