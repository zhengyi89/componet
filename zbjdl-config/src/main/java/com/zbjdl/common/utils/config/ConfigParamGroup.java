package com.zbjdl.common.utils.config;

import java.util.List;

public interface ConfigParamGroup<T> {
    ConfigParam<T> getConfig(String var1);

    List<ConfigParam<T>> values();

    List<String> keys();
}
