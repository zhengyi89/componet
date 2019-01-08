/**
 * Copyright: Copyright (c)2018
 * 
 */
package com.zbjdl.common.utils.queryview.interceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: Spring MVC 上下文</p>
 * <p>Description: 仿 Struts2 的 ActionContext</p>
 * <p>Copyright: Copyright (c)2018</p>
 * 
 *
 * 
 * @version 0.1, 14-5-1 18:37
 */
public class ControllerContext implements Serializable {

    private static ThreadLocal controllerContext = new ThreadLocal();

    private Map<Object, Object> context;

    public static final String REQUEST = "javax.servlet.http.HttpServletRequest";
    public static final String RESPONSE = "javax.servlet.http.HttpServletResponse";
    public static final String SESSION = "javax.servlet.http.HttpSession";
    public static final String APPLICATION = "javax.faces.application.Application";

    public ControllerContext(Map<Object, Object> context) {
        this.context = context;
    }

    public static void setContext(ControllerContext context) {
        controllerContext.set(context);
    }

    public static ControllerContext getContext() {
        ControllerContext context = (ControllerContext) controllerContext.get();
        if (null == context) {
            context = new ControllerContext(new HashMap<Object, Object>());
            setContext(context);
        }
        return context;
    }

    public Map<Object, Object> getContextMap() {
        return this.context;
    }

    public void setContextMap(Map<Object, Object> contextMap) {
        getContext().context = contextMap;
    }

    public Object get(Object key) {
        return context.get(key);
    }

    public void put(Object key, Object value) {
        context.put(key, value);
    }

    public void setRequest(HttpServletRequest request) {
        put(REQUEST, request);
    }

    public HttpServletRequest getRequest() {
        return (HttpServletRequest) get(REQUEST);
    }

    public HttpServletResponse getResponse() {
        return (HttpServletResponse) get(RESPONSE);
    }

    public void setResponse(HttpServletResponse response) {
        put(RESPONSE, response);
    }

    public HttpSession getSession() {
        return getRequest().getSession();
    }

    public ServletContext getApplication() {
        return getSession().getServletContext();
    }

}
