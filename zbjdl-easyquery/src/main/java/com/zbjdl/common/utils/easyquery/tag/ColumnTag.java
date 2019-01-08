//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.easyquery.tag;

import com.zbjdl.common.utils.easyquery.EasyQueryException;
import com.zbjdl.common.utils.easyquery.EasyQueryStack;
import com.zbjdl.common.utils.easyquery.bean.ColumnBean;
import com.zbjdl.common.utils.easyquery.tag.ajax.AjaxQueryTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import org.apache.struts2.views.jsp.StrutsBodyTagSupport;

public class ColumnTag extends StrutsBodyTagSupport {
    private static final long serialVersionUID = -5402280006310639564L;
    protected ColumnBean bean = new ColumnBean();

    public ColumnTag() {
    }

    public int doStartTag() throws JspException {
        return 2;
    }

    public int doAfterBody() throws JspException {
        this.bean.setBody(this.getBodyContent().getString());
        this.getBodyContent().clearBody();
        return 0;
    }

    public int doEndTag() throws JspException {
        Tag t = findAncestorWithClass(this, ListableTag.class);
        if (t == null) {
            throw new EasyQueryException("no parent listable tag found !!");
        } else {
            if (t instanceof AjaxQueryTag) {
                AjaxQueryTag parent = (AjaxQueryTag)t;
                parent.addColumnBean(this.bean);
            } else {
                EasyQueryStack stack = EasyQueryStack.getEasyQueryStack(this.getStack());
                this.bean.populateParams(stack);
            }

            this.bean = new ColumnBean();
            return 6;
        }
    }

    public void setTitle(String title) {
        this.bean.setTitle(title);
    }

    public void setValue(String value) {
        this.bean.setValue(value);
    }

    public void setWidth(String width) {
        this.bean.setWidth(width);
    }

    public void setAlign(String align) {
        this.bean.setAlign(align);
    }

    public void setSortable(String sortable) {
        this.bean.setSortable(sortable);
    }

    public void setTitlehtml(String titlehtml) {
        this.bean.setTitlehtml(titlehtml);
    }

    public void setHtml(String html) {
        this.bean.setHtml(html);
    }

    public void setOrderBy(String orderBy) {
        this.bean.setOrderBy(orderBy);
    }

    public void setCssClass(String cssClass) {
        this.bean.setCssClass(cssClass);
    }

    public void setTitleCssClass(String titleCssClass) {
        this.bean.setTitleCssClass(titleCssClass);
    }

    public void setEscape(boolean escape) {
        this.bean.setEscape(escape);
    }

    public void setUnion(String union) {
        this.bean.setUnion(union);
    }
}
