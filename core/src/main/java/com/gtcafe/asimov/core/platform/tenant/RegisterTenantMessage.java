package com.gtcafe.asimov.core.platform.tenant;

import java.util.UUID;

import com.gtcafe.asimov.core.constants.KindConstants;
import com.gtcafe.asimov.core.system.event.IMessage;

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
        this.data = data;
        this.kind = KindConstants.PLATFORM_SAYHELLO;
        this.id = UUID.randomUUID().toString();
    }

    public RegisterTenantMessage() {
        this.id = UUID.randomUUID().toString();
        this.data = null;
        this.kind = KindConstants.PLATFORM_SAYHELLO;
    }

}
