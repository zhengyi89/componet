package com.zbjdl.common.utils.queryview;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;

public class QueryViewApplicationContextHelper implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3587205612959740110L;


	private static ApplicationContext applicationContext ;

    public static void setContext(ApplicationContext context) {
    	applicationContext = context;
    }

    public static ApplicationContext getContext() {
    	ApplicationContext context = applicationContext;
       
        return context;
    }

}