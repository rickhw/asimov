package com.gtcafe.asimov.platform.tenant.consumer;

import com.gtcafe.asimov.platform.tenant.model.Tenant;
import com.gtcafe.asimov.system.queue.model.Event;

public class TenantEvent extends Event<Tenant> {
    
    public TenantEvent(Tenant data) {
        super(data);
    }

    public TenantEvent() {
        super();
    }
}
