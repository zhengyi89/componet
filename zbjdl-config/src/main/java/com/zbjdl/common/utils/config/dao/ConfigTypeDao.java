package com.zbjdl.common.utils.config.dao;

import com.zbjdl.common.persistence.GenericDao;
import com.zbjdl.common.utils.config.entity.ConfigType;

public interface ConfigTypeDao extends GenericDao<ConfigType> {
    ConfigType queryByCode(String var1);
}
