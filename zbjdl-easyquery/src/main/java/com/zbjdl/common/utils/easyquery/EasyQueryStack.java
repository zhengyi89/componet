package com.zbjdl.common.utils.easyquery;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.easyquery.action.QueryForm;
import com.zbjdl.common.utils.easyquery.bean.NoResultBean;
import com.zbjdl.common.utils.easyquery.bean.ParamBean;
import com.zbjdl.utils.query.QueryParam;
import com.zbjdl.utils.query.QueryResult;
import com.zbjdl.utils.query.QueryService;
import com.opensymphony.xwork2.util.TextParseUtil;
import com.opensymphony.xwork2.util.ValueStack;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ComponentUtils;
import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.apache.struts2.views.freemarker.ScopesHashModel;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class EasyQueryStack {
    public static String DEFAULT_CSSCLASS = "easyquery";
    private static FreemarkerManager freemarkerManager = new FreemarkerManager();
    private ValueStack valueStack;
    private int index = 0;
    private List columnsParam = new ArrayList();
    private Map<String, String> rowParam;
    private int startIndex = 0;
    private Map<ParamBean, Object> queryParams;
    private NoResultBean noResultBean;

    private EasyQueryStack(ValueStack valueStack) {
        this.valueStack = valueStack;
    }

    public static EasyQueryStack getEasyQueryStack(ValueStack valueStack) {
        EasyQueryStack stack = (EasyQueryStack)valueStack.findValue("#_easyquery_stack", EasyQueryStack.class);
        if (stack == null) {
            stack = new EasyQueryStack(valueStack);
            valueStack.setValue("#_easyquery_stack", stack);
        }

        return stack;
    }

    public void clearEasyQueryStack() {
        this.valueStack.setValue("#_easyquery_stack", (Object)null);
        this.columnsParam.clear();
        this.columnsParam = null;
        if (this.rowParam != null) {
            this.rowParam.clear();
            this.rowParam = null;
        }

        this.valueStack = null;
    }

    public void setNoResultBean(NoResultBean noResultBean) {
        this.noResultBean = noResultBean;
    }

    public List getColumnsParam() {
        return this.columnsParam;
    }

    public Map<String, String> getRowParam() {
        return this.rowParam;
    }

    public void addColumnParam(Map columnParam) {
        this.columnsParam.add(columnParam);
    }

    public void setRowParam(Map rowParam) {
        this.rowParam = rowParam;
    }

    public String findString(String expr) {
        return (String)this.findValue(expr, String.class);
    }

    public Object findObjValue(String expr) {
        expr = ComponentUtils.stripExpressionIfAltSyntax(this.valueStack, expr);
        return this.valueStack.findValue(expr);
    }

    public String findValue(String expr, boolean escape) {
        expr = ComponentUtils.stripExpressionIfAltSyntax(this.valueStack, expr);
        Object value = this.valueStack.findValue(expr);
        String result = value == null ? null : value.toString();
        if (escape) {
            result = StringEscapeUtils.escapeHtml(result);
        }

        return result;
    }

    public String findValue(String expr) {
        return this.findValue(expr, false);
    }

    public <T> T findValue(String expr, Class<T> toType) {
        if (CheckUtils.isEmpty(expr)) {
            return null;
        } else if (ComponentUtils.altSyntax(this.valueStack) && toType == String.class) {
            return (T) TextParseUtil.translateVariables('%', expr, this.valueStack);
        } else {
            expr = ComponentUtils.stripExpressionIfAltSyntax(this.valueStack, expr);
            return (T) this.valueStack.findValue(expr, toType);
        }
    }

    public void setContextValue(String name, Object value) {
        this.valueStack.setValue("#" + name, value);
    }

    public void pop() {
        this.valueStack.pop();
    }

    public void push(Object obj) {
        this.valueStack.push(obj);
        ++this.index;
        this.valueStack.setValue("#rowindex", this.index);
        this.valueStack.setValue("#globalindex", this.startIndex - 1 + this.index);
        String rowparity = this.index % 2 == 1 ? "odd" : "even";
        this.valueStack.setValue("#rowparity", rowparity);
    }

    public String buildHtml(Map htmlInfo) {
        if (htmlInfo == null) {
            return null;
        } else {
            String _html = (String)htmlInfo.get("html");
            String _width = (String)htmlInfo.get("width");
            String _height = (String)htmlInfo.get("height");
            String _align = (String)htmlInfo.get("align");
            String _cssClass = (String)htmlInfo.get("cssClass");
            String elementType = (String)htmlInfo.get("elementType");
            if ("row".equals(elementType)) {
                elementType = "row_" + this.findString("%{#rowparity}");
            }

            String html = "";
            if (!CheckUtils.isEmpty(_html)) {
                html = html + this.findString(_html);
            }

            String tmp_html = html.replaceAll(" ", "").toLowerCase();
            if (!CheckUtils.isEmpty(_width) && tmp_html.indexOf("width=") < 0) {
                html = html + " width=\"" + this.retriveStr(this.findString(_width)) + "\"";
            }

            if (!CheckUtils.isEmpty(_height) && tmp_html.indexOf("height=") < 0) {
                html = html + " height=\"" + this.retriveStr(this.findString(_height)) + "\"";
            }

            if (!CheckUtils.isEmpty(_align) && tmp_html.indexOf("align=") < 0) {
                html = html + " align=\"" + this.retriveStr(this.findString(_align)) + "\"";
            }

            if (tmp_html.indexOf("class=") < 0) {
                String cssClass = DEFAULT_CSSCLASS + "_" + elementType;
                if (!CheckUtils.isEmpty(_cssClass)) {
                    cssClass = this.retriveStr(this.findString(_cssClass)) + "_" + elementType;
                }

                html = html + " class=\"" + cssClass + "\"";
            }

            return html;
        }
    }

    public String retriveStr(String str) {
        return str == null ? "" : str;
    }

    public boolean isFirstQuery() {
        Object obj = this.findValue("#_easyquery_flag", Object.class);
        if (obj == null) {
            this.valueStack.setValue("#_easyquery_flag", true);
            return true;
        } else {
            return false;
        }
    }

    public void addQueryParam(ParamBean key, Object value) {
        if (this.queryParams == null) {
            this.queryParams = new HashMap();
        }

        this.queryParams.put(key, value);
    }

    public String parseFtlTemplate(Map root, String templateName) throws IOException, TemplateException {
        this.valueStack.getContext().put("_ftl_root_obj", root);

        String var3;
        try {
            var3 = FtlUtils.generateHtml(root, templateName);
        } finally {
            this.valueStack.getContext().remove("_ftl_root_obj");
        }

        return var3;
    }

    public String parseFtlStr(String ftlSource) {
        return this.parseFtlStr(ftlSource, false);
    }

    public String parseFtlStr(String ftlSource, boolean escape) {
        try {
            ServletContext servletContext = ServletActionContext.getServletContext();
            HttpServletRequest request = ServletActionContext.getRequest();
            HttpServletResponse response = ServletActionContext.getResponse();
            Object action = this.valueStack.getContext().get("action");
            Configuration config = freemarkerManager.getConfiguration(servletContext);
            ScopesHashModel root = freemarkerManager.buildTemplateModel(this.valueStack, action, servletContext, request, response, config.getObjectWrapper());
            String result = FtlUtils.parseFtl(root, ftlSource);
            if (escape) {
                result = StringEscapeUtils.escapeHtml(result);
            }

            return result;
        } catch (Throwable var10) {
            return null;
        }
    }

    public QueryForm doQuery(String queryKey, String queryService, int pageSize, boolean doSum) {
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
        QueryService service = (QueryService)context.getBean(queryService);
        QueryForm queryForm = QueryForm.buildForm(ServletActionContext.getRequest(), queryKey);
        queryForm.setPageSize(pageSize);
        Map params = queryForm.getQueryParams();
        if ("false".equals(params.get("_queryable"))) {
            return queryForm;
        } else {
            Object key;
            if (this.queryParams != null) {
                Iterator var9 = this.queryParams.keySet().iterator();

                label44:
                while(true) {
                    ParamBean pb;
                    do {
                        if (!var9.hasNext()) {
                            break label44;
                        }

                        pb = (ParamBean)var9.next();
                        key = params.get(pb.getName());
                    } while(!CheckUtils.isEmpty(key) && !pb.isPreferred());

                    params.put(pb.getName(), this.queryParams.get(pb));
                }
            }

            Map _params = new HashMap();
            if (params != null) {
                _params.putAll(params);
                Iterator var13 = params.keySet().iterator();

                while(var13.hasNext()) {
                    key = var13.next();
                    Object value = params.get(key);
                    if (value != null && value instanceof String) {
                        _params.put(key, value.toString().trim());
                    }
                }
            }

            QueryParam param = queryForm.getQueryParm();
            param.setParams(_params);
            param.setDoSum(doSum);
            QueryResult result = service.query(queryKey, param);
            queryForm.setQueryResult(result);
            if (result != null) {
                this.startIndex = result.getStartIndex().intValue();
            }

            return queryForm;
        }
    }

    public String getNoResultHtml() {
        if (this.noResultBean == null) {
            return null;
        } else if (!CheckUtils.isEmpty(this.noResultBean.getValue())) {
            return this.findValue(this.noResultBean.getValue());
        } else {
            return !CheckUtils.isEmpty(this.noResultBean.getBody()) ? this.parseFtlStr(this.noResultBean.getBody()) : null;
        }
    }

    static {
        freemarkerManager.setEncoding("UTF-8");
    }
}
