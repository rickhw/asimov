package com.gtcafe.asimov.core.platform.hello;

import com.gtcafe.asimov.core.constants.KindConstants;
import com.gtcafe.asimov.core.domain.event.IMessage;
import java.util.UUID;

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
