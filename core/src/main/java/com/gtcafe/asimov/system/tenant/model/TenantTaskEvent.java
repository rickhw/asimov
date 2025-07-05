package com.gtcafe.asimov.system.tenant.model;

import com.gtcafe.asimov.system.task.schema.Task;

public class TenantTaskEvent extends Task<Tenant> {
    
    public TenantTaskEvent(Tenant data) {
        setData(data);
    }
}
