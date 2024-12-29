package com.gtcafe.asimov.platform.tenant.consumer;

import com.gtcafe.asimov.platform.task.schema.Task;
import com.gtcafe.asimov.platform.tenant.model.Tenant;

public class TenantTaskEvent extends Task<Tenant> {
    
    public TenantTaskEvent(Tenant data) {
        setData(data);
    }
}
