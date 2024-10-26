package com.gtcafe.asimov.core.system.event2.tenant;

import com.gtcafe.asimov.core.system.event2.Event;

public class TenantEvent extends Event<TenantMetadata, TenantAttribute> {

    public TenantEvent() {
        super.setKind("Tenant");
        super.setApiVersion("v1");
        this.setMetadata(new TenantMetadata());
        this.setAttributes(new TenantAttribute());
    }
}
