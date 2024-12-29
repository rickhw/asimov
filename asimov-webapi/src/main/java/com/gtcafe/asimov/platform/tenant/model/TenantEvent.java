package com.gtcafe.asimov.platform.tenant.model;

import com.gtcafe.asimov.system.queue.event.Event;

import lombok.Builder;

@Builder
public class TenantEvent extends Event<Tenant> {
    
    public TenantEvent(Tenant data) {
        super(data);
    }

    public TenantEvent() {
        super();
    }


    // public static TenantEvent create(Tenant data) {
    //     return TenantEvent.builder()
    //         .setData(data)
    //         // .data(data)
    //         .build();
    // }
}
