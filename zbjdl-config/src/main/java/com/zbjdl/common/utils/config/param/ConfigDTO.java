package com.zbjdl.common.utils.config.param;

import com.zbjdl.common.utils.config.enumtype.ValueDataTypeEnum;
import com.zbjdl.common.utils.config.enumtype.ValueTypeEnum;

import java.io.Serializable;
import java.util.Date;

public class ConfigDTO implements Serializable {
    private static final long serialVersionUID = 6660470796349612617L;
    private Long id;
    private String namespace;
    private String type;
    private String configKey;
    private ValueDataTypeEnum valueDataType;
    private ValueTypeEnum valueType;
    private String valueDataTypeStr;
    private String valueTypeStr;
    private String value;
    private Boolean updatable = false;
    private Boolean deletable = false;
    private Long creatorId;
    private Date createDate = new Date();
    private Long updatorId;
    private Date updateDate = new Date();
    private String description;
    private String systemId;

    public ConfigDTO() {
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

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUpdatorId() {
        return this.updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValueDataTypeStr() {
        return this.valueDataTypeStr;
    }

    public void setValueDataTypeStr(String valueDataTypeStr) {
        this.valueDataTypeStr = valueDataTypeStr;
    }

    public String getValueTypeStr() {
        return this.valueTypeStr;
    }

    public void setValueTypeStr(String valueTypeStr) {
        this.valueTypeStr = valueTypeStr;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
}
