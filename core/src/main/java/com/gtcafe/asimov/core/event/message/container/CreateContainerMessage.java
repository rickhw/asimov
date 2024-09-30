package com.gtcafe.asimov.core.event.message.container;

import com.gtcafe.asimov.core.event.IMessage;

public class CreateContainerMessage implements IMessage {
    private final String content;

    public CreateContainerMessage(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}
