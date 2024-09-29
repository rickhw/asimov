package com.gtcafe.asimov.core.event.message;

import com.gtcafe.asimov.core.event.Message;

// TypeAMessage 实现 Message 接口
public class CreateContainerMessage implements Message {
    private final String messageContent;

    public CreateContainerMessage(String messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public String getMessageContent() {
        return messageContent;
    }
}
