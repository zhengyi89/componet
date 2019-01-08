//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.ext.perf;

import com.zbjdl.common.utils.event.impl.EventEngineImpl;
import com.zbjdl.common.utils.event.lang.diagnostic.Profiler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PerformanceLogger {
    private static final Logger logger = LoggerFactory.getLogger("PerfLogger");
    private int threshold = 100;
    private List<String> includeMethod;
    private List<String> excludeMethod;

    public PerformanceLogger() {
    }

    public Object doIntercept(Object object, String className, String methodName) throws Throwable {
        boolean topContext = false;
        String methodInfo = className + "." + methodName;
        boolean var15 = false;

        Object var6;
        try {
            var15 = true;
            if (this.matched(methodName)) {
                topContext = Profiler.getEntry() == null;
                if (topContext) {
                    Profiler.start(methodInfo);
                } else {
                    Profiler.enter(methodInfo);
                }
            }

            var6 = this.proceed(object);
            var15 = false;
        } finally {
            if (var15) {
                if (this.matched(methodName)) {
                    Profiler.release();
                }

                long elapseTime = Profiler.getDuration();
                if (topContext && elapseTime > (long)this.threshold) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(methodInfo);
                    sb.append(" elapsed time : ");
                    sb.append(elapseTime);
                    sb.append(" ms.\n");
                    sb.append(Profiler.dump(EventEngineImpl.PERF_PREFIX));
                    logger.info(sb.toString());
                }

                if (topContext) {
                    Profiler.reset();
                }

            }
        }

        if (this.matched(methodName)) {
            Profiler.release();
        }

        long elapseTime = Profiler.getDuration();
        if (topContext && elapseTime > (long)this.threshold) {
            StringBuilder sb = new StringBuilder();
            sb.append(methodInfo);
            sb.append(" elapsed time : ");
            sb.append(elapseTime);
            sb.append(" ms.\n");
            sb.append(Profiler.dump(EventEngineImpl.PERF_PREFIX));
            logger.info(sb.toString());
        }

        if (topContext) {
            Profiler.reset();
        }

        return var6;
    }

    protected abstract Object proceed(Object var1) throws Throwable;

    public void setIncludeMethod(List<String> methodNames) {
        if (methodNames != null && !methodNames.isEmpty()) {
            this.includeMethod = new ArrayList();
            Iterator var2 = methodNames.iterator();

            while(var2.hasNext()) {
                String pattern = (String)var2.next();
                if (pattern != null) {
                    this.includeMethod.add(pattern.replaceAll("\\.*\\*", ".*"));
                }
            }
        }

    }

    public void setExcludeMethod(List<String> methodNames) {
        if (methodNames != null && !methodNames.isEmpty()) {
            this.excludeMethod = new ArrayList();
            Iterator var2 = methodNames.iterator();

            while(var2.hasNext()) {
                String pattern = (String)var2.next();
                if (pattern != null) {
                    this.excludeMethod.add(pattern.replaceAll("\\.*\\*", ".*"));
                }
            }
        }

    }

    private boolean matched(String methodName) {
        Iterator var2;
        String pattern;
        if (this.includeMethod != null && !this.includeMethod.isEmpty()) {
            var2 = this.includeMethod.iterator();

            do {
                if (!var2.hasNext()) {
                    return false;
                }

                pattern = (String)var2.next();
            } while(!methodName.matches(pattern));

            return true;
        } else if (this.excludeMethod != null && !this.excludeMethod.isEmpty()) {
            var2 = this.excludeMethod.iterator();

            do {
                if (!var2.hasNext()) {
                    return true;
                }

                pattern = (String)var2.next();
            } while(!methodName.matches(pattern));

            return false;
        } else {
            return true;
        }
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
