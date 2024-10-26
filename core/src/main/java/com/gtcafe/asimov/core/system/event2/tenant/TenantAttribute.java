package com.gtcafe.asimov.core.system.event2.tenant;

import com.gtcafe.asimov.core.system.event2.Attribute;

public class TenantAttribute extends Attribute {

    public TenantAttribute() {
        this.put("tenantName", "DefaultTenant");
        this.put("region", "DefaultRegion");
    }
}
