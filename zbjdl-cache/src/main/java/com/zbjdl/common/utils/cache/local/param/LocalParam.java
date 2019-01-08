//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.cache.local.param;

public class LocalParam {
    private boolean eternal;
    private int timeToIdleSeconds;
    private int timeToLiveSeconds;

    public LocalParam(boolean eternal, int timeToIdleSeconds, int timeToLiveSeconds) {
        this.eternal = eternal;
        this.timeToIdleSeconds = timeToIdleSeconds;
        this.timeToLiveSeconds = timeToLiveSeconds;
    }

    public boolean isEternal() {
        return this.eternal;
    }

    public void setEternal(boolean eternal) {
        this.eternal = eternal;
    }

    public int getTimeToIdleSeconds() {
        return this.timeToIdleSeconds;
    }

    public void setTimeToIdleSeconds(int timeToIdleSeconds) {
        this.timeToIdleSeconds = timeToIdleSeconds;
    }

    public int getTimeToLiveSeconds() {
        return this.timeToLiveSeconds;
    }

    public void setTimeToLiveSeconds(int timeToLiveSeconds) {
        this.timeToLiveSeconds = timeToLiveSeconds;
    }
}
