package com.gtcafe.asimov.system.context;

import lombok.Getter;
import lombok.Setter;

public class HttpRequestContext {

    // public static final String X_REQUEST_ID = "X-Request-Id";

    private static final ThreadLocal<HttpRequestContext> CONTEXT = new ThreadLocal<>();

    @Setter @Getter
    private String requestId;

    private HttpRequestContext(String requestId) {
        this.requestId = requestId;
    }

    public static HttpRequestContext GetCurrentContext() {
        return CONTEXT.get();
    }

    public static void SetCurrentContext(HttpRequestContext tenantContext) {
        CONTEXT.set(tenantContext);
    }

    public static void Clear() {
        CONTEXT.remove();
    }

    public static HttpRequestContext of(String requestId) {
        return new HttpRequestContext(requestId);
    }

}