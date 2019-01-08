package com.zbjdl.common.utils.easyquery.action;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.easyquery.AjaxQueryContext;
import com.zbjdl.common.utils.easyquery.EasyQueryException;
import com.zbjdl.common.utils.easyquery.EasyQueryStack;
import com.zbjdl.common.utils.easyquery.bean.AjaxQueryBean;
import com.zbjdl.common.utils.easyquery.bean.ColumnBean;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;

public class AjaxQueryResult extends StrutsResultSupport {
    private String defaultEncoding;

    public AjaxQueryResult() {
    }

    protected void doExecute(String locationArg, ActionInvocation invocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String key = request.getRequestURI();
        AjaxQueryContext queryContext = AjaxQueryContext.getAjaxQueryContext(ServletActionContext.getServletContext());
        AjaxQueryBean bean = queryContext.getAjaxQueryBean(key);
        if (bean == null) {
            throw new EasyQueryException("no AjaxQueryTag defined : " + key);
        } else if (CheckUtils.isEmpty(bean.getColumnsBean())) {
            throw new EasyQueryException("no ColumnTag defined : " + key);
        } else {
            EasyQueryStack queryStack = EasyQueryStack.getEasyQueryStack(invocation.getStack());

            try {
                Iterator var9 = bean.getColumnsBean().iterator();

                while(var9.hasNext()) {
                    ColumnBean columnBean = (ColumnBean)var9.next();
                    columnBean.populateParams(queryStack);
                }

                if (bean.getRowBean() != null) {
                    bean.getRowBean().populateParams(queryStack);
                }

                String _template = "defaultajaxlistable";
                if (!CheckUtils.isEmpty(bean.getTableBean().getTemplate())) {
                    _template = queryStack.findString(bean.getTableBean().getTemplate());
                }

                if (this.defaultEncoding != null) {
                    response.setCharacterEncoding(this.defaultEncoding);
                }

                String encoding = response.getCharacterEncoding();
                Map tableParam = bean.getTableBean().populateParams(queryStack);
                String text = queryStack.parseFtlTemplate(tableParam, _template);
                response.setContentType("text/html;charset=" + encoding);
                Writer writer = new OutputStreamWriter(response.getOutputStream(), encoding);
                writer.write(text);
                if (bean.getTableBean().isPaging()) {
                    String _pagingTemplate = "defaultajaxlistable_paging";
                    if (!CheckUtils.isEmpty(bean.getTableBean().getPagingTemplate())) {
                        _pagingTemplate = queryStack.findString(bean.getTableBean().getPagingTemplate());
                    }

                    Map pagingParam = bean.getPagingBean().populateParams(queryStack);
                    pagingParam.putAll(tableParam);
                    String pagingText = queryStack.parseFtlTemplate(pagingParam, _pagingTemplate);
                    writer.write(pagingText);
                }

                writer.flush();
            } finally {
                queryStack.clearEasyQueryStack();
            }

        }
    }

    @Inject("struts.i18n.encoding")
    public void setDefaultEncoding(String val) {
        this.defaultEncoding = val;
    }
}
