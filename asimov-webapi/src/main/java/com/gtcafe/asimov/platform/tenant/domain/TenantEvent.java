package com.gtcafe.asimov.platform.tenant.domain;

import com.gtcafe.asimov.core.system.event3.Event;
import com.gtcafe.asimov.platform.tenant.domain.model.Tenant;

public class TenantEvent extends Event<Tenant> {
    
    public TenantEvent(Tenant data) {
        super(data);
    }

    public TenantEvent() {
        super();
    }

}
