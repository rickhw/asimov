package com.gtcafe.asimov.core.common.context;

import lombok.Getter;
import lombok.Setter;

public class TenantContext {

    public static final String X_TENANT_ID = "X-Tenant-Id";
    public static final String X_APP_NAME = "X-AppName";
    public static final String X_ROLE_NAME = "X-RoleName";
    // public static final String X_REQUEST_ID = "X-Request-Id";

    private static final ThreadLocal<TenantContext> CONTEXT = new ThreadLocal<>();

    @Setter @Getter
    private String tenantId;

    @Setter @Getter
    private String appName;

    @Setter @Getter
    private String roleName;

    // @Setter @Getter
    // private String requestId;

    private TenantContext(String tenantId, String appName, String roleNamed) {
        this.tenantId = tenantId;
        this.appName = appName;
        this.roleName = roleName;
        // this.requestId = requestId;
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

    public static TenantContext of(String tenantId, String appName, String roleName) {
        return new TenantContext(tenantId, appName, roleName);
    }

}