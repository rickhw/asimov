package com.gtcafe.asimov.core.platform.hello;

import java.util.UUID;

import com.gtcafe.asimov.core.system.constants.KindConstants;
import com.gtcafe.asimov.core.system.event.IMessage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class SayHelloMessage implements IMessage {
    @Getter @Setter
    private String id;

    @Getter @Setter
    private String kind;

    @Getter @Setter
    private String data;

    public SayHelloMessage(String data) {
        this.data = data;
        this.kind = KindConstants.PLATFORM_SAYHELLO;
        this.id = UUID.randomUUID().toString();
    }

    public SayHelloMessage() {
        this.id = UUID.randomUUID().toString();
        this.data = null;
        this.kind = KindConstants.PLATFORM_SAYHELLO;
    }

}
