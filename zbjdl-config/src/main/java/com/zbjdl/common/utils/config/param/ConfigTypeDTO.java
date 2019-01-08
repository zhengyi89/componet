package com.zbjdl.common.utils.config.param;

import java.io.Serializable;

public class ConfigTypeDTO implements Serializable {
    private static final long serialVersionUID = -5272160770230955084L;
    private String code;
    private String description;

    public ConfigTypeDTO() {
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
