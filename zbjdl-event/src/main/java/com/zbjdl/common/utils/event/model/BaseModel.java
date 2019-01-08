package com.zbjdl.common.utils.event.model;

import com.zbjdl.common.utils.event.utils.SystemConfigUtils;
import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class BaseModel implements Serializable {
    private static final long serialVersionUID = -4466565769030195137L;
    private transient int group = SystemConfigUtils.getAppGroupID();

    public BaseModel() {
    }

    public int getGroup() {
        return this.group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String toString() {
        try {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        } catch (Exception var2) {
            return super.toString();
        }
    }
}
