package com.zbjdl.common.utils.config.dao;

import com.zbjdl.common.persistence.GenericDao;
import com.zbjdl.common.utils.config.entity.ConfigVersion;

import java.util.List;

public interface ConfigVersionDao extends GenericDao<ConfigVersion> {
    List<ConfigVersion> queryByTypes(String[] var1);

    ConfigVersion queryByType(String var1);

    List<ConfigVersion> queryAll();
}
