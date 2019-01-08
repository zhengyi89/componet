//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.easyquery.bean;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.easyquery.EasyQueryException;
import com.zbjdl.common.utils.easyquery.EasyQueryStack;
import com.zbjdl.common.utils.easyquery.action.QueryForm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableBean implements TagComponentBean {
    private String formId;
    private String queryResult;
    private String width;
    private String align;
    private String html;
    private String cssClass;
    private String template;
    private String queryUrl;
    private String ajaxDiv;
    private String pagingTemplate;
    private boolean paging = true;
    private boolean autoQuery;
    private String queryKey;
    private String queryService = "queryService";
    private int pageSize = 20;

    public TableBean() {
    }

    public String getFormId() {
        return this.formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getQueryResult() {
        return this.queryResult;
    }

    public void setQueryResult(String queryResult) {
        this.queryResult = queryResult;
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

    public String getTemplate() {
        return this.template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getQueryUrl() {
        return this.queryUrl;
    }

    public void setQueryUrl(String queryUrl) {
        this.queryUrl = queryUrl;
    }

    public String getAjaxDiv() {
        return this.ajaxDiv;
    }

    public void setAjaxDiv(String ajaxdiv) {
        this.ajaxDiv = ajaxdiv;
    }

    public boolean isPaging() {
        return this.paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public String getPagingTemplate() {
        return this.pagingTemplate;
    }

    public void setPagingTemplate(String pagingTemplate) {
        this.pagingTemplate = pagingTemplate;
    }

    public boolean isAutoQuery() {
        return this.autoQuery;
    }

    public void setAutoQuery(boolean autoQuery) {
        this.autoQuery = autoQuery;
    }

    public String getQueryKey() {
        return this.queryKey;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public String getQueryService() {
        return this.queryService;
    }

    public void setQueryService(String queryService) {
        this.queryService = queryService;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Map populateParams(EasyQueryStack queryStack) {
        List columnsParam = queryStack.getColumnsParam();
        CheckUtils.notEmpty(columnsParam, "columnsParam");
        Map rowParam = queryStack.getRowParam();
        RowBean _queryForm;
        if (rowParam == null) {
            _queryForm = new RowBean();
            rowParam = _queryForm.populateParams(queryStack);
        }

        _queryForm = null;
        String _formId;
        QueryForm queryForm;
        if (this.queryResult != null) {
            queryForm = (QueryForm)queryStack.findValue(this.queryResult, QueryForm.class);
            if (queryForm == null) {
                throw new EasyQueryException("query result not found!");
            }
        } else {
            _formId = queryStack.findString(this.queryKey);
            if (_formId == null) {
                throw new EasyQueryException("queryResult or queryKey must be specified");
            }

            queryForm = queryStack.doQuery(_formId, this.queryService, this.pageSize, false);
        }

        _formId = null;
        if (!CheckUtils.isEmpty(this.formId)) {
            _formId = queryStack.findString(this.formId);
        }

        String _queryUrl = null;
        if (!CheckUtils.isEmpty(this.queryUrl)) {
            _queryUrl = queryStack.findString(this.queryUrl);
        }

        String _ajaxDiv = null;
        if (!CheckUtils.isEmpty(this.ajaxDiv)) {
            _ajaxDiv = queryStack.findString(this.ajaxDiv);
        }

        Map<String, Object> listableInfo = new HashMap();
        listableInfo.put("formId", _formId);
        listableInfo.put("queryUrl", _queryUrl);
        listableInfo.put("ajaxDiv", _ajaxDiv);
        listableInfo.put("paging", this.paging);
        Map tmp = new HashMap();
        tmp.put("width", this.width);
        tmp.put("align", this.align);
        tmp.put("html", this.html);
        tmp.put("cssClass", this.cssClass);
        tmp.put("elementType", "table");
        String _html = queryStack.buildHtml(tmp);
        listableInfo.put("html", _html);
        Map data = new HashMap();
        data.put("tableBean", listableInfo);
        data.put("queryForm", queryForm);
        data.put("columnBeans", columnsParam);
        data.put("rowBean", rowParam);
        data.put("stack", queryStack);
        data.put("_firstquery", queryStack.isFirstQuery());
        return data;
    }
}
