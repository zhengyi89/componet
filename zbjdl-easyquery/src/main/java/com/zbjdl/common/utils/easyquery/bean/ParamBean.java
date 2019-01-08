//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.easyquery.bean;

import com.zbjdl.common.utils.easyquery.EasyQueryStack;
import java.util.Map;

public class ParamBean implements TagComponentBean {
    private String name;
    private String value;
    private boolean preferred = true;

    public ParamBean() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isPreferred() {
        return this.preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }

    public Map populateParams(EasyQueryStack queryStack) {
        Object obj = queryStack.findObjValue(this.value);
        queryStack.addQueryParam(this, obj);
        return null;
    }
}
