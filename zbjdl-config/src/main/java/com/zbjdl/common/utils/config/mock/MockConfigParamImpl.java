package com.zbjdl.common.utils.config.mock;

import com.zbjdl.common.utils.config.ConfigParam;

public class MockConfigParamImpl<T> implements ConfigParam {
    private T value;

    public MockConfigParamImpl(T val) {
        this.value = val;
    }

    public T getValue() {
        return this.value;
    }

    public String toString() {
        return this.value == null ? null : this.value.toString();
    }
}
