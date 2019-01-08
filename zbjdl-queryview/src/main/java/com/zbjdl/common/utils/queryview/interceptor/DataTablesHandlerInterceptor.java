/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: DataTables 拦截器</p>
 * <p>Description:
 * 1.捕获对于 Mapping 到 /** 且请求后缀为 .json 的所有 GET 请求
 * 2.根据 queryKey 查找是否配置了查询语句
 * 3.根据查询语句封装返回结果</p>
 * <p>Copyright: Copyright (c)2018</p>
 * 
 *
 * 
 * @version 0.1, 14-5-15 9:31
 */
public class DataTablesHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataTablesHandlerInterceptor.class);

    private static String requestMethod = "GET";

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        LOGGER.debug(request.getRequestURI());
        // Only requestMethod allow to init query content
        if (!request.getMethod().equalsIgnoreCase(requestMethod) && !"ALL".equals(requestMethod)) {
            return true;
        }

        //注入Request/Response对象至SpringMVC执行上下文对象中
        ControllerContext.getContext().setRequest(request);
        ControllerContext.getContext().setResponse(response);

        // TODO 暂不支持
        if (request.getRequestURI().endsWith(".query")) {
            LOGGER.debug("根据 queryKey 查找是否配置了查询语句并封装返回结果，请求参数：{}", request.getParameterNames());
            return false;
        }

        return super.preHandle(request, response, handler);
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

}
