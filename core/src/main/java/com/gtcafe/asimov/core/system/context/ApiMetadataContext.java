package com.gtcafe.asimov.core.system.context;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

public class ApiMetadataContext {

    @Getter @Setter
    private String operationId;

    @Getter @Setter
    private String method;

    @Getter @Setter
    private String uri;

    @Getter @Setter
    private String kind; 

    private static final ThreadLocal<ApiMetadataContext> CONTEXT = new ThreadLocal<>();

    private static HashMap<String, String> operationIdMap = new HashMap();
    private static HashMap<String, String> kindMap = new HashMap();
    
    // TODO: as bean load from json file
    static {
        operationIdMap.put("GET:/", "root");
        operationIdMap.put("GET:/version", "slogan");
        operationIdMap.put("GET:/metrics", "metrics");
        operationIdMap.put("GET:/health", "health");

        kindMap.put("GET:/", "system.Entry");
        kindMap.put("GET:/version", "system.Version");
        kindMap.put("GET:/metrics", "system.Metric");
        kindMap.put("GET:/health", "system.Health");
    }

    private ApiMetadataContext(String method, String uri) {
        this.method = method;
        this.uri = uri;
        try {
            this.operationId = operationIdMap.get(method + ":" + uri);
            this.kind = kindMap.get(method + ":" + uri);    
        } catch (Exception e) {
            this.operationId = "unknown";
            this.kind = "unknown";
        }
    }

    public static ApiMetadataContext getCurrentContext() {
        return CONTEXT.get();
    }

    public static void setCurrentContext(ApiMetadataContext tenantContext) {
        CONTEXT.set(tenantContext);
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static ApiMetadataContext of(String method, String uri) {
        return new ApiMetadataContext(method, uri);
    }
}
