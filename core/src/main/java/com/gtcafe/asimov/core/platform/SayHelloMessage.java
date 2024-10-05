package com.gtcafe.asimov.core.platform;

import com.gtcafe.asimov.core.event.IMessage;

public class SayHelloMessage implements IMessage {
    private final String content;

    public SayHelloMessage(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}
