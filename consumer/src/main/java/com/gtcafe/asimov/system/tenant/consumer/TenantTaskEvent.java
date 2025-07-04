package com.gtcafe.asimov.system.tenant.consumer;

import com.gtcafe.asimov.system.task.schema.Task;
import com.gtcafe.asimov.system.tenant.model.Tenant;

public class TenantTaskEvent extends Task<Tenant> {
    
    public TenantTaskEvent(Tenant data) {
        setData(data);
    }
}
