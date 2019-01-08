//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.easyquery.tag.ajax;

import com.zbjdl.common.utils.easyquery.AjaxQueryContext;
import com.zbjdl.common.utils.easyquery.EasyQueryStack;
import com.zbjdl.common.utils.easyquery.bean.AjaxQueryBean;
import com.zbjdl.common.utils.easyquery.bean.ColumnBean;
import com.zbjdl.common.utils.easyquery.bean.PagingBean;
import com.zbjdl.common.utils.easyquery.bean.RowBean;
import com.zbjdl.common.utils.easyquery.bean.TableBean;
import com.zbjdl.common.utils.easyquery.tag.ListableTag;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

public class AjaxQueryTag extends ListableTag {
    private AjaxQueryBean ajaxQueryBean;

    public AjaxQueryTag() {
    }

    public void addColumnBean(ColumnBean columnBean) {
        this.ajaxQueryBean.addColumnBean(columnBean);
    }

    public void setRowBean(RowBean rowBean) {
        this.ajaxQueryBean.setRowBean(rowBean);
    }

    public int doStartTag() throws JspException {
        AjaxQueryContext queryContext = AjaxQueryContext.getAjaxQueryContext(this.pageContext.getServletContext());
        if (!this.bean.getQueryUrl().startsWith("/")) {
            String fullurl = ((HttpServletRequest)this.pageContext.getRequest()).getContextPath() + "/" + this.bean.getQueryUrl();
            this.bean.setQueryUrl(fullurl);
        }

        this.ajaxQueryBean = queryContext.getAjaxQueryBean(this.bean.getQueryUrl(), this.hashCode());
        if (this.ajaxQueryBean != null) {
            return 0;
        } else {
            this.ajaxQueryBean = new AjaxQueryBean(this.hashCode());
            this.ajaxQueryBean.setTableBean(this.bean);
            PagingBean pagingBean = new PagingBean();
            pagingBean.setCssClass(this.bean.getCssClass());
            this.ajaxQueryBean.setPagingBean(pagingBean);
            queryContext.setAjaxQueryBean(this.bean.getQueryUrl(), this.ajaxQueryBean);
            return 1;
        }
    }

    public int doEndTag() throws JspException {
        EasyQueryStack queryStack = EasyQueryStack.getEasyQueryStack(this.getStack());

        try {
            String text = queryStack.parseFtlTemplate(this.ajaxQueryBean.populateParams(queryStack), "ajaxeasyqueryjs");
            this.pageContext.getOut().write(text);
        } catch (Exception var6) {
            throw new JspException(var6);
        } finally {
            queryStack.clearEasyQueryStack();
            this.bean = new TableBean();
        }

        return 6;
    }

    public void setAutoQuery(boolean autoQuery) {
        this.bean.setAutoQuery(autoQuery);
    }

    public void setQueryUrl(String queryUrl) {
        this.bean.setQueryUrl(queryUrl);
    }

    public void setAjaxDiv(String ajaxdiv) {
        this.bean.setAjaxDiv(ajaxdiv);
    }

    public void setPaging(boolean paging) {
        this.bean.setPaging(paging);
    }

    public void setPagingTemplate(String pagingTemplate) {
        this.bean.setPagingTemplate(pagingTemplate);
    }
}
