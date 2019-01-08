//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.ext.perf;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class PerformanceInterceptor extends PerformanceLogger implements MethodInterceptor {
    public PerformanceInterceptor() {
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        String className = invocation.getMethod().getDeclaringClass().getSimpleName();
        String methodName = invocation.getMethod().getName();
        return this.doIntercept(invocation, className, methodName);
    }

    protected Object proceed(Object object) throws Throwable {
        MethodInvocation invocation = (MethodInvocation)object;
        return invocation.proceed();
    }
}
