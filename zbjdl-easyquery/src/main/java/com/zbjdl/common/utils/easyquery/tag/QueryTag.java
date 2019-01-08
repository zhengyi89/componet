//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.easyquery.tag;

import com.zbjdl.common.utils.easyquery.EasyQueryStack;
import com.zbjdl.common.utils.easyquery.action.QueryForm;
import javax.servlet.jsp.JspException;
import org.apache.struts2.views.jsp.StrutsBodyTagSupport;

public class QueryTag extends StrutsBodyTagSupport {
    private String queryKey;
    private String queryService = "queryService";
    private String id;
    private int pageSize;
    private boolean doSum = true;

    public QueryTag() {
    }

    public int doEndTag() throws JspException {
        EasyQueryStack stack = EasyQueryStack.getEasyQueryStack(this.getStack());
        QueryForm queryForm = stack.doQuery(this.queryKey, this.queryService, this.pageSize, this.doSum);
        stack.setContextValue(this.id, queryForm);
        return 6;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public void setQueryService(String queryService) {
        this.queryService = queryService;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setDoSum(boolean doSum) {
        this.doSum = doSum;
    }
}
