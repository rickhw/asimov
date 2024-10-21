package com.gtcafe.asimov.core.platform.hello;

import com.gtcafe.asimov.core.domain.event.IMessage;
import java.util.UUID;

public class SayHelloMessage implements IMessage {
    private String id;
    private String kind;
    private String data;

    public SayHelloMessage(String data) {
        this.data = data;
        this.id = UUID.randomUUID().toString();
    }

    public SayHelloMessage() {
        this.data = null;
        this.id = null;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String getKind() {
        return this.kind;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getData() {
        return this.data;
    }
}
