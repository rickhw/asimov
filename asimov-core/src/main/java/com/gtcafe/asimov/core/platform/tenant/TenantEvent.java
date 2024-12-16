package com.gtcafe.asimov.core.platform.tenant;

import com.gtcafe.asimov.core.system.event.Event;

public class TenantEvent extends Event<Tenant> {
    
    public TenantEvent(Tenant data) {
        super(data);
    }

    public TenantEvent() {
        super();
    }

}
