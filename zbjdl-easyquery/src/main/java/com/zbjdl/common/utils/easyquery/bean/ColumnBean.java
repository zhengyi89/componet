package com.zbjdl.common.utils.easyquery.bean;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.easyquery.EasyQueryStack;
import java.util.HashMap;
import java.util.Map;

public class ColumnBean implements TagComponentBean {
    private String title;
    private String value;
    private String width;
    private String align;
    private String sortable;
    private String titlehtml;
    private String html;
    private String orderBy;
    private String cssClass;
    private String titleCssClass;
    private String body;
    private boolean escape = true;
    private String union;

    public ColumnBean() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getAlign() {
        return this.align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getSortable() {
        return this.sortable;
    }

    public void setSortable(String sortable) {
        this.sortable = sortable;
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

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getCssClass() {
        return this.cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getTitleCssClass() {
        return this.titleCssClass;
    }

    public void setTitleCssClass(String titleCssClass) {
        this.titleCssClass = titleCssClass;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isEscape() {
        return this.escape;
    }

    public void setEscape(boolean escape) {
        this.escape = escape;
    }

    public String getUnion() {
        return this.union;
    }

    public void setUnion(String union) {
        this.union = union;
    }

    public Map populateParams(EasyQueryStack queryStack) {
        Map colInfo = new HashMap();
        String _title = queryStack.findString(this.title);
        colInfo.put("title", _title);
        colInfo.put("value", this.value);
        colInfo.put("width", this.width);
        colInfo.put("align", this.align);
        colInfo.put("html", this.titlehtml);
        colInfo.put("cssClass", this.titleCssClass);
        colInfo.put("elementType", "th");
        String _titleHtml = queryStack.buildHtml(colInfo);
        colInfo.put("titleHtml", _titleHtml);
        colInfo.put("elementType", "td");
        colInfo.put("html", this.html);
        colInfo.put("cssClass", this.cssClass);
        Boolean _sortable = null;
        if (!CheckUtils.isEmpty(this.sortable)) {
            _sortable = (Boolean)queryStack.findValue(this.sortable, Boolean.class);
        }

        if (_sortable == null) {
            _sortable = false;
        }

        colInfo.put("sortable", _sortable);
        if (_sortable.booleanValue()) {
            String _orderBy = null;
            if (CheckUtils.isEmpty(this.orderBy)) {
                _orderBy = this.value;
            } else {
                _orderBy = this.orderBy;
            }

            colInfo.put("orderBy", _orderBy);
        }

        colInfo.put("body", this.body);
        colInfo.put("escape", this.escape);
        colInfo.put("union", this.union);
        queryStack.addColumnParam(colInfo);
        return colInfo;
    }
}
