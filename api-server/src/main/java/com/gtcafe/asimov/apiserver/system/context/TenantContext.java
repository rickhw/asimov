package com.gtcafe.asimov.apiserver.system.context;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class TenantContext {
    private static final ThreadLocal<TenantContext> CONTEXT = new ThreadLocal<>();

    @Setter @Getter
    private String tenantId;

    @Setter @Getter
    private String appName;

    @Setter @Getter
    private String roleName;

    @Setter @Getter
    private String requestId;

    private TenantContext(String tenantId, String appName, String roleName, String requestId) {
        this.tenantId = tenantId;
        this.appName = appName;
        this.roleName = roleName;
        this.requestId = requestId;
    }

    public static TenantContext getCurrentContext() {
        return CONTEXT.get();
    }

    public static void setCurrentContext(TenantContext tenantContext) {
        CONTEXT.set(tenantContext);
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static TenantContext of(String tenantId, String appName, String roleName, String requestId) {
        return new TenantContext(tenantId, appName, roleName, requestId);
    }

}