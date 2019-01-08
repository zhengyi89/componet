/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.queryview.interceptor.ControllerContext;
import com.zbjdl.common.utils.queryview.tags.QueryUIContext;
import com.zbjdl.common.utils.queryview.tags.bean.PreparedParamBean;
import com.zbjdl.utils.query.QueryParam;
import com.zbjdl.utils.query.QueryResult;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @since：2012-5-18 下午07:16:00
 * @version:
 */
public class QueryForm {

    /**
     * 当前请求的页数
     */
    private Integer currentPage;
    private String orderby;
    private Boolean asc;
    private Map<String, Object> httpParams;
    private String queryKey;

    private int pageSize;
    private QueryResult queryResult;
    private int totalPage;
    private int totalCount;
    private boolean autoTrim = true;
    private List<PreparedParamBean> preparedParams;
    private boolean doSum;
    private List queryData;

    public boolean hasPrevious() {
        return currentPage > 1;
    }

    public boolean hasNext() {
        return currentPage < totalPage;
    }

    public void setQueryResult(QueryResult result) {
        this.queryResult = result;
        if (null != result && null != result.getData()) {
            this.totalCount = result.getTotalCount().intValue();
            this.totalPage = totalCount / pageSize;
            if (result.getTotalCount() % pageSize > 0) {
                totalPage++;
            }
            if (result.getData() instanceof List) {
                queryData = (List) result.getData();
            } else {
                queryData = new ArrayList(result.getData());
            }
        }
    }

    public void addPreparedParam(String name, Object value) {
        addPreparedParam(name, value, true);
    }

    public void addPreparedParam(String name, Object value, boolean preferred) {
        if (preparedParams == null) {
            preparedParams = new ArrayList<PreparedParamBean>();
        }
        preparedParams.add(new PreparedParamBean(name, value, preferred));
    }

    public void setPreparedParams(List<PreparedParamBean> list) {
        preparedParams = list;
    }

    public static QueryForm buildForm(String queryKey) {
        RequestParams httpParams = RequestParams.buildRequestParams();
        return buildForm(queryKey, httpParams);
    }

    public static QueryForm buildForm(String queryKey, RequestParams requestParams) {
        QueryForm form = new QueryForm();
        String p_cpage = "_cpage";
        String p_orderby = "_orderby";
        String p_asc = "_asc";
        if (!CheckUtils.isEmpty(queryKey)) {
            p_cpage = queryKey + "." + p_cpage;
            p_orderby = queryKey + "." + p_orderby;
            p_asc = queryKey + "." + p_asc;
        }
        form.queryKey = queryKey;
        form.currentPage = requestParams.getIntegerParam(p_cpage);
        if (form.currentPage == null || form.currentPage < 1) {
            form.currentPage = 1;
        }
        form.orderby = requestParams.getStringParam(p_orderby);
        if (!CheckUtils.isEmpty(form.orderby)) {
            form.asc = requestParams.getBooleanParam(p_asc);
            if (form.asc == null) {
                form.asc = true;
            }
            QueryUIContext context = getContext();
            context.setOrderby(form.orderby);
            context.setOrderByAsc(form.asc);
            form.orderby = getOrderByValue(form.orderby, form.asc);
        }
        form.httpParams = requestParams.getParams();
        return form;
    }

    public QueryParam createQueryParm() {
        QueryParam q = new QueryParam();
        q.setStartIndex(getStratIndex());
        q.setMaxSize(pageSize);
        q.setOrderByColumn(orderby);
        q.setIsAsc(asc);
        q.setDoSum(doSum);

        Map<String, Object> trimedParams = new HashMap<String, Object>();
        trimedParams.putAll(httpParams);
        if (preparedParams != null) {
            for (PreparedParamBean pb : preparedParams) {
                Object v = trimedParams.get(pb.getName());
                if (CheckUtils.isEmpty(v) || pb.isPreferred()) {
                    trimedParams.put(pb.getName(), pb.getValue());
                }
            }
        }
        if (autoTrim) {
            for (String key : trimedParams.keySet()) {
                Object value = trimedParams.get(key);
                trimedParams.put(key, doTrim(value));
            }
        }
        q.setParams(trimedParams);
        return q;
    }

    /**
     * 获取order by 值
     *
     * @param orderbyKey
     * @return
     */
    @SuppressWarnings("unchecked")
    private static String getOrderByValue(String orderbyKey, boolean asc) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        return ((Map<String, String>) webApplicationContext.getServletContext().getAttribute(QueryUIContext.ORDER_BY_MAP)).get(orderbyKey);
    }

    private static QueryUIContext getContext() {
        return (QueryUIContext) ControllerContext.getContext().get(QueryUIContext.QUERYUI_CONTEXT_KEY);
    }

    public int getFirstPage() {
        return 1;
    }

    public int getLastPage() {
        return totalPage;
    }

    public int getPreviousPage() {
        return currentPage <= 1 ? currentPage : currentPage - 1;
    }

    public int getNextPage() {
        return currentPage >= totalPage ? currentPage : currentPage + 1;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public QueryResult getQueryResult() {
        return queryResult;
    }

    public List getQueryData() {
        return queryData;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStratIndex() {
        return pageSize * (currentPage - 1) + 1;
    }

    public String getOrderby() {
        return orderby;
    }

    public Boolean getAsc() {
        return asc;
    }

    public Map<String, Object> getHttpParams() {
        return httpParams;
    }

    public String getQueryKey() {
        return queryKey;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public boolean isAutoTrim() {
        return autoTrim;
    }

    public void setAutoTrim(boolean autoTrim) {
        this.autoTrim = autoTrim;
    }

    public boolean isDoSum() {
        return doSum;
    }

    public void setDoSum(boolean doSum) {
        this.doSum = doSum;
    }

    private Object doTrim(Object value) {
        if (value != null && value instanceof String) {
            return ((String) value).trim();
        }
        return value;
    }

}
