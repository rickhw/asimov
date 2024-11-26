package com.gtcafe.asimov.core.system.bean;

public class RequestIdBean {
    private final String requestId;
    public RequestIdBean() {
        this.requestId = java.util.UUID.randomUUID().toString();
    }

    public String getRequestId() {
        return requestId;
    }
}
