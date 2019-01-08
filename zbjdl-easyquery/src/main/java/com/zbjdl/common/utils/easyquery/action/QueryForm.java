package com.zbjdl.common.utils.easyquery.action;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.easyquery.EasyQueryException;
import com.zbjdl.common.utils.easyquery.RequestParams;
import com.zbjdl.utils.query.QueryParam;
import com.zbjdl.utils.query.QueryResult;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class QueryForm {
    private Integer currentPage;
    private String orderby;
    private Boolean asc;
    private Map<String, Object> queryParams;
    private String queryKey;
    private int pageSize;
    private QueryResult queryResult;
    private int totalPage;
    private int totalCount;

    public QueryForm() {
    }

    public boolean hasPrevious() {
        return this.currentPage.intValue() > 1;
    }

    public boolean hasNext() {
        return this.currentPage.intValue() < this.totalPage;
    }

    public void setQueryResult(QueryResult result) {
        if (result != null) {
            this.queryResult = result;
            this.totalCount = result.getTotalCount().intValue();
            this.pageSize = result.getMaxFetchSize().intValue();
            this.totalPage = this.totalCount / this.pageSize;
            this.queryKey = result.getQueryKey();
            if (result.getTotalCount().longValue() % (long)this.pageSize > 0L) {
                ++this.totalPage;
            }

        }
    }

    public int getFirstPage() {
        return 1;
    }

    public int getLastPage() {
        return this.totalPage;
    }

    public int getPreviousPage() {
        return this.currentPage.intValue() <= 1 ? this.currentPage.intValue() : this.currentPage.intValue() - 1;
    }

    public int getNextPage() {
        return this.currentPage.intValue() >= this.totalPage ? this.currentPage.intValue() : this.currentPage.intValue() + 1;
    }

    public Integer getCurrentPage() {
        return this.currentPage;
    }

    public QueryResult getQueryResult() {
        return this.queryResult;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStratIndex() {
        return this.pageSize * (this.currentPage.intValue() - 1) + 1;
    }

    public String getOrderby() {
        return this.orderby;
    }

    public Boolean getAsc() {
        return this.asc;
    }

    public Map<String, Object> getQueryParams() {
        return this.queryParams;
    }

    public String getQueryKey() {
        return this.queryKey;
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public static QueryForm buildForm(HttpServletRequest request, int pageSize) {
        QueryForm form = buildForm(request);
        form.pageSize = pageSize;
        return form;
    }

    public static QueryForm buildForm(HttpServletRequest request) {
        RequestParams httpParams = RequestParams.buildRequestParams(request);
        Object queryKey = httpParams.getParam("queryKey");
        if (queryKey != null && queryKey.getClass().isArray()) {
            throw new EasyQueryException("queryKey must be specified");
        } else {
            return buildForm(httpParams, queryKey != null ? queryKey.toString() : null);
        }
    }

    public static QueryForm buildForm(HttpServletRequest request, String queryKey) {
        RequestParams httpParams = RequestParams.buildRequestParams(request);
        return buildForm(httpParams, queryKey);
    }

    private static QueryForm buildForm(RequestParams httpParams, String queryKey) {
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
        form.currentPage = httpParams.getIntegerParam(p_cpage);
        if (form.currentPage == null) {
            form.currentPage = Integer.valueOf(1);
        }

        form.orderby = httpParams.getStringParam(p_orderby);
        if (!CheckUtils.isEmpty(form.orderby)) {
            form.asc = httpParams.getBooleanParam(p_asc);
            if (form.asc == null) {
                form.asc = true;
            }
        }

        form.queryParams = httpParams.getParams();
        return form;
    }

    public QueryParam getQueryParm() {
        QueryParam q = new QueryParam();
        q.setStartIndex(this.getStratIndex());
        q.setMaxSize(this.pageSize);
        q.setOrderByColumn(this.orderby);
        q.setIsAsc(this.asc);
        q.setParams(this.queryParams);
        return q;
    }
}
