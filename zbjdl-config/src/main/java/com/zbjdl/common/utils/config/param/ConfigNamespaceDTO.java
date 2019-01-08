package com.zbjdl.common.utils.config.param;

import java.io.Serializable;

public class ConfigNamespaceDTO implements Serializable {
    private static final long serialVersionUID = -3839427351890886709L;
    private String code;
    private String description;

    public ConfigNamespaceDTO() {
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
