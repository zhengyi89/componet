//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.exception;

import com.zbjdl.common.exception.BaseException;

public class EventRuntimeException extends BaseException {
    private static final long serialVersionUID = 2202551582776037772L;
    private int errorCode;
    private String message;
    private Object[] parameters;

    public EventRuntimeException(int errorCode) {
        this.errorCode = errorCode;
        this.message = ExceptionMsgUtils.getMessage(errorCode);
    }

    public EventRuntimeException(int errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public EventRuntimeException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = ExceptionMsgUtils.getMessage(errorCode);
    }

    public EventRuntimeException(int errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }

    public EventRuntimeException(int errorCode, Object... args) {
        this.errorCode = errorCode;
        this.message = ExceptionMsgUtils.getMessage(errorCode);
        this.parameters = args;
    }

    public EventRuntimeException(int errorCode, String message, Object... args) {
        this.errorCode = errorCode;
        this.message = message;
        this.parameters = args;
    }

    public EventRuntimeException(int errorCode, Throwable cause, Object... args) {
        super(cause);
        this.errorCode = errorCode;
        this.message = ExceptionMsgUtils.getMessage(errorCode);
        this.parameters = args;
    }

    public EventRuntimeException(int errorCode, String message, Throwable cause, Object... args) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
        this.parameters = args;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return this.toString();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object[] getParameters() {
        return this.parameters;
    }

    public void setParameters(Object... parameters) {
        this.parameters = parameters;
    }

    public String toString() {
        String s = this.getClass().getName();
        return s + ": EC = " + this.errorCode + (this.message != null ? ": MSG = " + this.message : "");
    }
}
