package com.gtcafe.asimov.core.system.context;

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


    private TenantContext(String tenantId, String appName, String roleName) {
        this.tenantId = tenantId;
        this.appName = appName;
        this.roleName = roleName;
    }

    public static TenantContext GetCurrentContext() {
        return CONTEXT.get();
    }

    public static void SetCurrentContext(TenantContext tenantContext) {
        CONTEXT.set(tenantContext);
    }

    public static void Clear() {
        CONTEXT.remove();
    }

    public static TenantContext of(String tenantId, String appName, String roleName) {
        return new TenantContext(tenantId, appName, roleName);
    }

}