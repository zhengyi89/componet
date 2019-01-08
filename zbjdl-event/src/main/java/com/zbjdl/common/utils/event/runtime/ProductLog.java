//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.runtime;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductLog {
    private static final Logger ERROR_LOG = LoggerFactory.getLogger(ProductLog.class);
    private String whos;
    private String action;
    private StringBuilder parameters;
    private String result;
    private String reason;
    private boolean isSuccess = true;
    private Logger logger;

    public ProductLog() {
    }

    public void reset() {
        this.parameters = null;
        this.reason = null;
    }

    public String getWhos() {
        return this.whos;
    }

    public void setWhos(String awhos) {
        this.whos = awhos;
    }

    public void appendWho(String who) {
        if (null == this.whos) {
            this.whos = who;
        } else {
            this.whos = this.whos + "," + who;
        }

    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getParameters() {
        return null != this.parameters ? this.parameters.toString() : "";
    }

    public StringBuilder appendParameter(String parameter) {
        if (null == this.parameters) {
            this.parameters = new StringBuilder(512);
        }

        if (StringUtils.isBlank(parameter)) {
            return this.parameters;
        } else {
            this.parameters.append(parameter);
            return this.parameters;
        }
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setResult(int code) {
        this.result = String.valueOf(code);
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void appendReason(String reason) {
        if (null == this.reason) {
            this.reason = reason;
        } else {
            this.reason = this.reason + "," + reason;
        }

    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void error(int result, String reason) {
        this.setSuccess(false);
        this.setResult(result);
        this.setReason(reason);
    }

    public void error(String result, String reason) {
        this.setSuccess(false);
        this.setResult(result);
        this.setReason(reason);
    }

    public void error(Throwable e) {
        this.error("exception", e);
    }

    public void error(int result, Throwable e) {
        this.error(String.valueOf(result), e);
    }

    public void error(String result, Throwable e) {
        String msg = e.getMessage();
        String name = e.getClass().getName();
        Throwable tt = ExceptionUtils.getCause(e);
        if (null != tt) {
            msg = tt.getMessage();
            name = tt.getClass().getName();
        }

        this.error(result, name + "," + msg);
    }

    public void success() {
        this.setSuccess(true);
    }

    public void success(String reason) {
        this.setSuccess(true);
        this.setReason(reason);
    }

    public void success(int result) {
        this.setSuccess(true);
        this.setResult(result);
    }

    public void success(int result, String reason) {
        this.setSuccess(true);
        this.setResult(result);
        this.setReason(reason);
    }

    public void log(ProductContext productContext, long times) {
        try {
            ProductRuntime runtime = productContext.getProductRuntime();
            Logger LOGGER = this.logger;
            if (runtime != null && null != LOGGER && StringUtils.isNotBlank(this.getWhos())) {
                ProductEnvironment env = runtime.getEnvironment();
                StringBuilder message = new StringBuilder();
                message.append("[th-");
                message.append(Thread.currentThread().getId());
                message.append("] | [");
                message.append(this.isSuccess() ? "true" : "false").append("] | [");
                message.append(String.valueOf(times)).append("] | ");
                message.append(this.getAction()).append(" | ");
                message.append(StringUtils.trimToEmpty(this.getWhos())).append(" | ");
                message.append(StringUtils.trimToEmpty(this.getResult())).append(" | ");
                message.append(StringUtils.trimToEmpty(this.getReason())).append(" | {");
                message.append(this.getParameters()).append("} | {");
                message.append(StringUtils.trimToEmpty(runtime.getUrl())).append("} | ");
                message.append(StringUtils.trimToEmpty(env.getClientIp()));
                LOGGER.info(message.toString());
            } else {
                StringBuilder message;
                if (null != LOGGER) {
                    message = new StringBuilder();
                    message.append("[th-");
                    message.append(Thread.currentThread().getId());
                    message.append("] | [");
                    message.append(this.isSuccess() ? "true" : "false").append("] | [");
                    message.append(String.valueOf(times)).append("] | ");
                    message.append(this.getAction()).append(" | ");
                    message.append("???").append(" | ");
                    message.append(StringUtils.trimToEmpty(this.getResult())).append(" | ");
                    message.append(StringUtils.trimToEmpty(this.getReason())).append(" | {");
                    message.append(this.getParameters()).append("} | {");
                    message.append("???").append("} | ");
                    message.append("???");
                    LOGGER.info(message.toString());
                } else if (null == LOGGER) {
                    message = new StringBuilder();
                    message.append("[th-");
                    message.append(Thread.currentThread().getId());
                    message.append("] | [");
                    message.append(this.isSuccess() ? "true" : "false").append("] | [");
                    message.append(String.valueOf(times)).append("] | ");
                    message.append(this.getAction()).append(" | ");
                    message.append("???").append(" | ");
                    message.append(StringUtils.trimToEmpty(this.getResult())).append(" | ");
                    message.append(StringUtils.trimToEmpty(this.getReason())).append(" | {");
                    message.append(this.getParameters()).append("} | {");
                    message.append("???").append("} | ");
                    message.append("???");
                    ERROR_LOG.info(message.toString());
                }
            }
        } catch (Exception var8) {
            ERROR_LOG.error("ProductLog", var8);
        }

    }
}
