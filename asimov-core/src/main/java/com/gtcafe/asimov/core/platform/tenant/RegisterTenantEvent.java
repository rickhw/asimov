package com.gtcafe.asimov.core.platform.tenant;

import com.gtcafe.asimov.core.system.event.Event;

import lombok.ToString;

@ToString
public class RegisterTenantEvent extends Event<RegisterTenantMessage> {
    public RegisterTenantEvent() {
        super();
        setData(new RegisterTenantMessage());
    }

    public RegisterTenantEvent(String message) {
        super();
        setData(new RegisterTenantMessage(message));
    }
}