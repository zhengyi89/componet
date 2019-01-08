//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.easyquery.bean;

import com.zbjdl.common.utils.easyquery.EasyQueryStack;
import java.util.Map;

public class NoResultBean implements TagComponentBean {
    private String value;
    private String body;

    public NoResultBean() {
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map populateParams(EasyQueryStack queryStack) {
        queryStack.setNoResultBean(this);
        return null;
    }
}
