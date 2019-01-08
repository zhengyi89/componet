//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.easyquery.bean;

import com.zbjdl.common.utils.easyquery.EasyQueryStack;
import java.util.HashMap;
import java.util.Map;

public class RowBean implements TagComponentBean {
    private String height;
    private String align;
    private String titlehtml;
    private String html;
    private String cssClass;

    public RowBean() {
    }

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAlign() {
        return this.align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getTitlehtml() {
        return this.titlehtml;
    }

    public void setTitlehtml(String titlehtml) {
        this.titlehtml = titlehtml;
    }

    public String getHtml() {
        return this.html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getCssClass() {
        return this.cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public Map populateParams(EasyQueryStack queryStack) {
        Map tmp = new HashMap();
        tmp.put("height", this.height);
        tmp.put("align", this.align);
        tmp.put("html", this.titlehtml);
        tmp.put("cssClass", this.cssClass);
        tmp.put("elementType", "trow");
        String _titlehtml = queryStack.buildHtml(tmp);
        tmp.put("html", this.html);
        tmp.put("elementType", "row");
        Map<String, String> rowInfo = new HashMap();
        rowInfo.put("titlehtml", _titlehtml);
        rowInfo.putAll(tmp);
        queryStack.setRowParam(rowInfo);
        return rowInfo;
    }
}
