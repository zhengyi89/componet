//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.easyquery.tag;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.easyquery.EasyQueryStack;
import com.zbjdl.common.utils.easyquery.bean.PagingBean;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.apache.struts2.views.jsp.StrutsBodyTagSupport;

public class PagingTag extends StrutsBodyTagSupport {
    protected PagingBean bean = new PagingBean();

    public PagingTag() {
    }

    public int doEndTag() throws JspException {
        EasyQueryStack stack = EasyQueryStack.getEasyQueryStack(this.getStack());

        try {
            String _template = "defaultlistable_paging";
            if (!CheckUtils.isEmpty(this.bean.getTemplate())) {
                _template = this.findString(this.bean.getTemplate());
            }

            Map pagingParam = this.bean.populateParams(stack);
            String text = stack.parseFtlTemplate(pagingParam, _template);
            this.pageContext.getOut().write(text);
        } catch (Exception var8) {
            throw new JspException(var8);
        } finally {
            stack.clearEasyQueryStack();
            this.bean = new PagingBean();
        }

        return 6;
    }

    public void setQueryResult(String queryResult) {
        this.bean.setQueryResult(queryResult);
    }

    public void setFormId(String formId) {
        this.bean.setFormId(formId);
    }

    public void setHtml(String html) {
        this.bean.setHtml(html);
    }

    public void setAlign(String align) {
        this.bean.setAlign(align);
    }

    public void setCssClass(String cssClass) {
        this.bean.setCssClass(cssClass);
    }

    public void setTemplate(String template) {
        this.bean.setTemplate(template);
    }
}
