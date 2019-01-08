//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.ext.perf;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.Ordered;

public class PerformanceAspect extends PerformanceLogger implements Ordered {
    private int order = 0;

    public PerformanceAspect() {
    }

    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        return this.doIntercept(joinPoint, className, methodName);
    }

    protected Object proceed(Object object) throws Throwable {
        ProceedingJoinPoint joinPoint = (ProceedingJoinPoint)object;
        return joinPoint.proceed();
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return this.order;
    }
}
