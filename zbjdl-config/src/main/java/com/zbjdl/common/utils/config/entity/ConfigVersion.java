package com.zbjdl.common.utils.config.entity;

import com.zbjdl.common.persistence.Entity;

public class ConfigVersion implements Entity<Long> {
    private static final long serialVersionUID = 867677528707405225L;
    private Long id;
    private String type;
    private Integer version;

    public ConfigVersion() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
