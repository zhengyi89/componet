package com.zbjdl.common.utils.config.entity;

import com.zbjdl.common.persistence.Entity;
import com.zbjdl.common.utils.config.enumtype.ValueDataTypeEnum;
import com.zbjdl.common.utils.config.enumtype.ValueTypeEnum;

import java.util.Date;

public class Config implements Entity<Long> {
    private static final long serialVersionUID = 7538204105072751108L;
    private Long id;
    private String namespace;
    private String type;
    private String configKey;
    private ValueDataTypeEnum valueDataType;
    private ValueTypeEnum valueType;
    private String value;
    private Boolean updatable = false;
    private Boolean deletable = false;
    private Long creatorId;
    private Date createDate = new Date();
    private Long updatorId;
    private Date updateDate = new Date();
    private String description = "";
    private String systemId;

    public Config() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConfigKey() {
        return this.configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public ValueDataTypeEnum getValueDataType() {
        return this.valueDataType;
    }

    public void setValueDataType(ValueDataTypeEnum valueDataType) {
        this.valueDataType = valueDataType;
    }

    public ValueTypeEnum getValueType() {
        return this.valueType;
    }

    public void setValueType(ValueTypeEnum valueType) {
        this.valueType = valueType;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getUpdatable() {
        return this.updatable;
    }

    public void setUpdatable(Boolean updatable) {
        this.updatable = updatable;
    }

    public Boolean getDeletable() {
        return this.deletable;
    }

    public void setDeletable(Boolean deletable) {
        this.deletable = deletable;
    }

    public Long getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getUpdatorId() {
        return this.updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
}
