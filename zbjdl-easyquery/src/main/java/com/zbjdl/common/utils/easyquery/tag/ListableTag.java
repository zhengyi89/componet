//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.easyquery.tag;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.easyquery.EasyQueryStack;
import com.zbjdl.common.utils.easyquery.FtlUtils;
import com.zbjdl.common.utils.easyquery.QueryTagConfigBean;
import com.zbjdl.common.utils.easyquery.action.QueryForm;
import com.zbjdl.common.utils.easyquery.bean.PagingBean;
import com.zbjdl.common.utils.easyquery.bean.TableBean;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.apache.struts2.views.jsp.StrutsBodyTagSupport;

public class ListableTag extends StrutsBodyTagSupport {
    protected TableBean bean = new TableBean();

    public ListableTag() {
    }

    public int doEndTag() throws JspException {
        EasyQueryStack stack = EasyQueryStack.getEasyQueryStack(this.getStack());

        try {
            Map listableParam = this.bean.populateParams(stack);
            QueryForm qForm = (QueryForm)listableParam.get("queryForm");
            String _template;
            if (qForm.getQueryResult() == null || CheckUtils.isEmpty(qForm.getQueryResult().getData())) {
                _template = stack.getNoResultHtml();
                if (_template != null) {
                    this.pageContext.getOut().write(_template);
                    byte var15 = 6;
                    return var15;
                }
            }

            if (Boolean.TRUE.equals(listableParam.get("_firstquery"))) {
                this.pageContext.getOut().write(FtlUtils.generateStaticHtml("defaultlistable.js"));
            }

            _template = QueryTagConfigBean.getDefaultTableTemplate();
            if (!CheckUtils.isEmpty(this.bean.getTemplate())) {
                _template = this.findString(this.bean.getTemplate());
            }

            String text = stack.parseFtlTemplate(listableParam, _template);
            this.pageContext.getOut().write(text);
            if (this.bean.isPaging()) {
                String _pagingTemplate = QueryTagConfigBean.getDefaultPagingTemplate();
                if (!CheckUtils.isEmpty(this.bean.getPagingTemplate())) {
                    _pagingTemplate = stack.findString(this.bean.getPagingTemplate());
                }

                PagingBean pagingBean = new PagingBean();
                pagingBean.setFormId(this.bean.getFormId());
                pagingBean.setQueryForm((QueryForm)listableParam.get("queryForm"));
                Map pagingParam = pagingBean.populateParams(stack);
                pagingParam.putAll(listableParam);
                String pagingText = stack.parseFtlTemplate(pagingParam, _pagingTemplate);
                this.pageContext.getOut().write(pagingText);
            }

            byte var16 = 6;
            return var16;
        } catch (Exception var13) {
            throw new JspException(var13);
        } finally {
            stack.clearEasyQueryStack();
            this.bean = new TableBean();
        }
    }

    public void setFormId(String formId) {
        this.bean.setFormId(formId);
    }

    public void setQueryResult(String queryResult) {
        this.bean.setQueryResult(queryResult);
    }

    public void setWidth(String width) {
        this.bean.setWidth(width);
    }

    public void setAlign(String align) {
        this.bean.setAlign(align);
    }

    public void setHtml(String html) {
        this.bean.setHtml(html);
    }

    public void setCssClass(String cssClass) {
        this.bean.setCssClass(cssClass);
    }

    public void setTemplate(String template) {
        this.bean.setTemplate(template);
    }

    public void setQueryKey(String queryKey) {
        this.bean.setQueryKey(queryKey);
    }

    public void setQueryService(String queryService) {
        this.bean.setQueryService(queryService);
    }

    public void setPageSize(int pageSize) {
        this.bean.setPageSize(pageSize);
    }

    public void setPagingTemplate(String pagingTemplate) {
        this.bean.setPagingTemplate(pagingTemplate);
    }

    public void setPaging(boolean paging) {
        this.bean.setPaging(paging);
    }
}
