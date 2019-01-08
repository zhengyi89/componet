package com.zbjdl.common.utils.config.param;

import java.io.Serializable;
import java.util.Date;

public class ConfigLogDTO implements Serializable {
    private static final long serialVersionUID = 2536752102281175485L;
    private String userName;
    private String function;
    private Date operateTime;
    private String configKey;
    private String description;

    public ConfigLogDTO() {
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFunction() {
        return this.function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public Date getOperateTime() {
        return this.operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getConfigKey() {
        return this.configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
