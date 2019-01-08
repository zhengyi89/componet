package com.zbjdl.common.utils.easyquery;

import com.zbjdl.common.utils.ConvertUtils;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class RequestParams {
    private Map params;

    public RequestParams() {
    }

    public static RequestParams buildRequestParams(HttpServletRequest request) {
        RequestParams requestParams = new RequestParams();
        requestParams.params = retrieveParams(request.getParameterMap());
        return requestParams;
    }

    public static Map retrieveParams(Map requestParams) {
        Map params = new HashMap();
        Iterator var2 = requestParams.keySet().iterator();

        while(var2.hasNext()) {
            Object key = var2.next();
            Object value = requestParams.get(key);
            if (value != null) {
                if (value.getClass().isArray()) {
                    int length = Array.getLength(value);
                    if (length == 1) {
                        params.put(key, Array.get(value, 0));
                    }

                    if (length > 1) {
                        params.put(key, value);
                    }
                } else {
                    params.put(key, value);
                }
            }
        }

        return params;
    }

    public Long getLongParam(String key) {
        return (Long)this.convertParam("long", key);
    }

    public Integer getIntegerParam(String key) {
        return (Integer)this.convertParam("int", key);
    }

    public Date getDateParam(String key) {
        return (Date)this.convertParam("date", key);
    }

    public Double getDoubleParam(String key) {
        return (Double)this.convertParam("double", key);
    }

    public Boolean getBooleanParam(String key) {
        return (Boolean)this.convertParam("boolean", key);
    }

    public Object convertParam(String toType, String key) {
        String value = this.getStringParam(key);
        if (value == null) {
            return null;
        } else {
            try {
                return ConvertUtils.convert(toType, value);
            } catch (Exception var5) {
                return null;
            }
        }
    }

    public String getStringParam(String key) {
        Object value = this.params.get(key);
        if (value == null) {
            return null;
        } else {
            return value instanceof String ? (String)value : ((String[])((String[])value))[0];
        }
    }

    public Object getParam(String key) {
        return this.params.get(key);
    }

    public Map getParams() {
        return this.params;
    }
}
