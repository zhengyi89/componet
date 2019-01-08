//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledTaskUtils {
    private static ScheduledExecutorService executorService;

    public ScheduledTaskUtils() {
    }

    private static ScheduledExecutorService getSharedScheduledExecutorService() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadScheduledExecutor();
        }

        return executorService;
    }

    public static void scheduleWithFixedDelay(Runnable command, long initialDelay, long delay) {
        getSharedScheduledExecutorService().scheduleWithFixedDelay(new ScheduledTaskUtils.Worker(command), initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    public static void schedule(Runnable command, long delay) {
        getSharedScheduledExecutorService().schedule(new ScheduledTaskUtils.Worker(command), delay, TimeUnit.MILLISECONDS);
    }

    private static class Worker implements Runnable {
        private Runnable adaptor;

        public Worker(Runnable adaptor) {
            this.adaptor = adaptor;
        }

        public void run() {
            Thread t = new Thread(this.adaptor);
            t.start();
        }
    }
}
