package com.zbjdl.common.utils.easyquery.bean;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.easyquery.EasyQueryStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AjaxQueryBean implements TagComponentBean {
    private TableBean tableBean;
    private List<ColumnBean> columnsBean = new ArrayList();
    private RowBean rowBean;
    private PagingBean pagingBean;
    private int version;

    public AjaxQueryBean(int v) {
        this.version = v;
    }

    public TableBean getTableBean() {
        return this.tableBean;
    }

    public void setTableBean(TableBean tableBean) {
        this.tableBean = tableBean;
    }

    public List<ColumnBean> getColumnsBean() {
        return this.columnsBean;
    }

    public void addColumnBean(ColumnBean columnBean) {
        this.columnsBean.add(columnBean);
    }

    public RowBean getRowBean() {
        return this.rowBean;
    }

    public void setRowBean(RowBean rowBean) {
        this.rowBean = rowBean;
    }

    public PagingBean getPagingBean() {
        return this.pagingBean;
    }

    public void setPagingBean(PagingBean pagingBean) {
        this.pagingBean = pagingBean;
    }

    public int getVersion() {
        return this.version;
    }

    public Map populateParams(EasyQueryStack queryStack) {
        Map data = new HashMap();
        String _formId = null;
        if (!CheckUtils.isEmpty(this.tableBean.getFormId())) {
            _formId = queryStack.findString(this.tableBean.getFormId());
        }

        String _queryUrl = null;
        if (!CheckUtils.isEmpty(this.tableBean.getQueryUrl())) {
            _queryUrl = queryStack.findString(this.tableBean.getQueryUrl());
        }

        String _ajaxDiv = null;
        if (!CheckUtils.isEmpty(this.tableBean.getAjaxDiv())) {
            _ajaxDiv = queryStack.findString(this.tableBean.getAjaxDiv());
        }

        data.put("formId", _formId);
        data.put("autoQuery", this.tableBean.isAutoQuery());
        data.put("queryUrl", _queryUrl);
        data.put("ajaxDiv", _ajaxDiv);
        data.put("_firstquery", queryStack.isFirstQuery());
        return data;
    }
}
