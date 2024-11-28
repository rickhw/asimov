package com.gtcafe.asimov.core.platform.tenant;

import java.util.UUID;

import com.gtcafe.asimov.system.constants.KindConstants;
import com.gtcafe.asimov.system.event.IMessage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class RegisterTenantMessage implements IMessage {
    @Getter @Setter
    private String id;

    @Getter @Setter
    private String kind;

    @Getter @Setter
    private String data;


    public RegisterTenantMessage(String data) {
        this.kind = KindConstants.PLATFORM_TENANT;
        this.id = UUID.randomUUID().toString();
        this.data = data;
    }

    public RegisterTenantMessage() {
        this.id = UUID.randomUUID().toString();
        this.kind = KindConstants.PLATFORM_TENANT;
        this.data = null;
    }

}
