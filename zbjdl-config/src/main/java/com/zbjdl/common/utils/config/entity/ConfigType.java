package com.zbjdl.common.utils.config.entity;

import com.zbjdl.common.persistence.Entity;

public class ConfigType implements Entity<Long> {
    private static final long serialVersionUID = 4284064889088677467L;
    private Long id;
    private String code;
    private String description;

    public ConfigType() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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
