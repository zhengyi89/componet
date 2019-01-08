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
import java.util.Map;

public class PagingBean implements TagComponentBean {
    private String queryResult;
    private String formId;
    private String html;
    private String align;
    private String cssClass;
    private String template;
    private QueryForm queryForm;

    public PagingBean() {
    }

    public String getQueryResult() {
        return this.queryResult;
    }

    public void setQueryResult(String queryResult) {
        this.queryResult = queryResult;
    }

    public String getFormId() {
        return this.formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getHtml() {
        return this.html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getAlign() {
        return this.align;
    }

    public void setAlign(String align) {
        this.align = align;
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

    public QueryForm getQueryForm() {
        return this.queryForm;
    }

    public void setQueryForm(QueryForm queryForm) {
        this.queryForm = queryForm;
    }

    public Map populateParams(EasyQueryStack queryStack) {
        QueryForm _queryForm = null;
        if (this.queryForm != null) {
            _queryForm = this.queryForm;
        } else if (!CheckUtils.isEmpty(this.queryResult)) {
            _queryForm = (QueryForm)queryStack.findValue(this.queryResult, QueryForm.class);
        }

        if (_queryForm == null) {
            throw new EasyQueryException("QueryForm must be specified!");
        } else {
            String _formId = queryStack.findString(this.formId);
            Map tmp = new HashMap();
            tmp.put("align", this.align);
            tmp.put("html", this.html);
            tmp.put("cssClass", this.cssClass);
            tmp.put("elementType", "pagingdiv");
            String pagingdivhtml = queryStack.buildHtml(tmp);
            tmp.put("elementType", "pagingdl");
            String pagingdlhtml = queryStack.buildHtml(tmp);
            tmp.put("elementType", "pagingselect");
            String pagingselecthtml = queryStack.buildHtml(tmp);
            tmp.put("elementType", "paginglink");
            String paginglinkhtml = queryStack.buildHtml(tmp);
            Map pageInfo = new HashMap();
            pageInfo.put("formId", _formId);
            pageInfo.put("pagingdivhtml", pagingdivhtml);
            pageInfo.put("pagingdlhtml", pagingdlhtml);
            pageInfo.put("pagingselecthtml", pagingselecthtml);
            pageInfo.put("paginglinkhtml", paginglinkhtml);
            pageInfo.put("stack", queryStack);
            Map data = new HashMap();
            data.put("pagingBean", pageInfo);
            data.put("queryForm", _queryForm);
            return data;
        }
    }
}
