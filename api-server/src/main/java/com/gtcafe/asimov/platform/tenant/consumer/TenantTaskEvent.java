package com.gtcafe.asimov.platform.tenant.consumer;

import com.gtcafe.asimov.platform.tenant.model.Tenant;
import com.gtcafe.asimov.system.task.schema.Task;

public class TenantTaskEvent extends Task<Tenant> {
    
    public TenantTaskEvent(Tenant data) {
        setData(data);
    }
}
