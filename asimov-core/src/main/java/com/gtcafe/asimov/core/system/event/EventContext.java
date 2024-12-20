package com.gtcafe.asimov.core.system.event;

import com.gtcafe.asimov.core.system.context.ApiMetadataContext;
import com.gtcafe.asimov.core.system.context.HttpRequestContext;
import com.gtcafe.asimov.core.system.context.TenantContext;

public class EventContext {

    // auth context
    private TenantContext tenantContext;

    // request
    private ApiMetadataContext apiMetadataContext;
    private HttpRequestContext httpRequestContext;

    public EventContext() {
        this.tenantContext = TenantContext.GetCurrentContext();
        this.apiMetadataContext = ApiMetadataContext.GetCurrentContext();
        this.httpRequestContext = HttpRequestContext.GetCurrentContext();
    }
}
