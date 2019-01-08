//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.easyquery.tag;

import com.zbjdl.common.utils.easyquery.EasyQueryException;
import com.zbjdl.common.utils.easyquery.EasyQueryStack;
import com.zbjdl.common.utils.easyquery.bean.RowBean;
import com.zbjdl.common.utils.easyquery.tag.ajax.AjaxQueryTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import org.apache.struts2.views.jsp.StrutsBodyTagSupport;

public class RowTag extends StrutsBodyTagSupport {
    protected RowBean bean = new RowBean();

    public RowTag() {
    }

    public int doEndTag() throws JspException {
        Tag t = findAncestorWithClass(this, ListableTag.class);
        if (t == null) {
            throw new EasyQueryException("no parent listable tag found !!");
        } else {
            if (t instanceof AjaxQueryTag) {
                AjaxQueryTag parent = (AjaxQueryTag)t;
                parent.setRowBean(this.bean);
            } else {
                EasyQueryStack stack = EasyQueryStack.getEasyQueryStack(this.getStack());
                this.bean.populateParams(stack);
            }

            this.bean = new RowBean();
            return 6;
        }
    }

    public void setHeight(String height) {
        this.bean.setHeight(height);
    }

    public void setAlign(String align) {
        this.bean.setAlign(align);
    }

    public void setTitlehtml(String titlehtml) {
        this.bean.setTitlehtml(titlehtml);
    }

    public void setHtml(String html) {
        this.bean.setHtml(html);
    }

    public void setCssClass(String cssClass) {
        this.bean.setCssClass(cssClass);
    }
}
