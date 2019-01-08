package com.zbjdl.common.utils.easyquery;

import com.zbjdl.common.utils.easyquery.bean.AjaxQueryBean;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;

public class AjaxQueryContext {
    private Map<String, AjaxQueryBean> context = new HashMap();

    public static AjaxQueryContext getAjaxQueryContext(ServletContext servletContext) {
        AjaxQueryContext queryContext = (AjaxQueryContext)servletContext.getAttribute("_ajax_query_context");
        if (queryContext == null) {
            queryContext = new AjaxQueryContext();
            servletContext.setAttribute("_ajax_query_context", queryContext);
        }

        return queryContext;
    }

    private AjaxQueryContext() {
    }

    public AjaxQueryBean getAjaxQueryBean(String key) {
        return (AjaxQueryBean)this.context.get(key);
    }

    public AjaxQueryBean getAjaxQueryBean(String key, int version) {
        AjaxQueryBean bean = (AjaxQueryBean)this.context.get(key);
        if (bean == null) {
            return null;
        } else if (bean.getVersion() != version) {
            this.context.remove(key);
            return null;
        } else {
            return bean;
        }
    }

    public void setAjaxQueryBean(String key, AjaxQueryBean bean) {
        this.context.put(key, bean);
    }
}
