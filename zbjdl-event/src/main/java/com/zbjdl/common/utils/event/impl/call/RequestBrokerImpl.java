//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.impl.call;

import com.zbjdl.common.utils.event.SokulaEvent;
import com.zbjdl.common.utils.event.exception.EventRuntimeException;
import com.zbjdl.common.utils.event.impl.EventEngineImpl;
import com.zbjdl.common.utils.event.impl.InvokeModel;
import com.zbjdl.common.utils.event.impl.InvokeModelEnum;
import com.zbjdl.common.utils.event.lang.diagnostic.Profiler;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestBrokerImpl extends AbstractBrokerImpl implements InvokeModel {
    private static final Logger loggerPerf = LoggerFactory.getLogger(RequestBrokerImpl.class);
    private ReqExecutorService rrExecutorService;

    public RequestBrokerImpl() {
    }

    public boolean accept(InvokeModelEnum type) {
        return type == InvokeModelEnum.REQUEST;
    }

    public Object broker(SokulaEvent event, CountDownLatch doneSignal) {
        Future<?> future = this.rrExecutorService.submit(new RequestBrokerImpl.RequestListenerCallable(event, doneSignal));
        return future;
    }

    public void setRrExecutorService(ReqExecutorService rrExecutorService) {
        this.rrExecutorService = rrExecutorService;
    }

    public class RequestListenerCallable implements Callable<Object> {
        private SokulaEvent event;
        private CountDownLatch doneSignal;

        public RequestListenerCallable(SokulaEvent event, CountDownLatch doneSignal) {
            this.event = event;
            this.doneSignal = doneSignal;
        }

        public Object call() {
            Profiler.start("EventEngineImpl.R-R.call");
            boolean var12 = false;

            Throwable var2;
            label106: {
                Object var1;
                try {
                    var12 = true;
                    if (this.event.getDelayHandleTime() < System.currentTimeMillis()) {
                        throw new EventRuntimeException(99201005);
                    }

                    RequestBrokerImpl.this.eventCall(this.event);
                    var1 = this.event.getReturnObject();
                    var12 = false;
                } catch (Throwable var13) {
                    this.event.setReturnObject(var13);
                    var2 = var13;
                    var12 = false;
                    break label106;
                } finally {
                    if (var12) {
                        if (null != this.doneSignal) {
                            this.doneSignal.countDown();
                        }

                        Profiler.release();
                        long elapseTimexx = Profiler.getDuration();
                        if (elapseTimexx > (long)EventEngineImpl.THRESHOLD) {
                            StringBuilder builderxx = new StringBuilder();
                            builderxx.append("URL:").append(this.event.getEventName());
                            builderxx.append(" over PMX = ").append(EventEngineImpl.THRESHOLD).append("ms,");
                            builderxx.append(" used P = ").append(elapseTimexx).append("ms.\r\n");
                            builderxx.append(Profiler.dump(EventEngineImpl.PERF_PREFIX));
                            RequestBrokerImpl.loggerPerf.info(builderxx.toString());
                        }

                        Profiler.reset();
                    }
                }

                if (null != this.doneSignal) {
                    this.doneSignal.countDown();
                }

                Profiler.release();
                long elapseTimex = Profiler.getDuration();
                if (elapseTimex > (long)EventEngineImpl.THRESHOLD) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("URL:").append(this.event.getEventName());
                    builder.append(" over PMX = ").append(EventEngineImpl.THRESHOLD).append("ms,");
                    builder.append(" used P = ").append(elapseTimex).append("ms.\r\n");
                    builder.append(Profiler.dump(EventEngineImpl.PERF_PREFIX));
                    RequestBrokerImpl.loggerPerf.info(builder.toString());
                }

                Profiler.reset();
                return var1;
            }

            if (null != this.doneSignal) {
                this.doneSignal.countDown();
            }

            Profiler.release();
            long elapseTime = Profiler.getDuration();
            if (elapseTime > (long)EventEngineImpl.THRESHOLD) {
                StringBuilder builderx = new StringBuilder();
                builderx.append("URL:").append(this.event.getEventName());
                builderx.append(" over PMX = ").append(EventEngineImpl.THRESHOLD).append("ms,");
                builderx.append(" used P = ").append(elapseTime).append("ms.\r\n");
                builderx.append(Profiler.dump(EventEngineImpl.PERF_PREFIX));
                RequestBrokerImpl.loggerPerf.info(builderx.toString());
            }

            Profiler.reset();
            return var2;
        }
    }
}
