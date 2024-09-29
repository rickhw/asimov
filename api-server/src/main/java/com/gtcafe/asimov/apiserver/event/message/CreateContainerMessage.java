package com.gtcafe.asimov.apiserver.event.message;

import com.gtcafe.asimov.apiserver.event.IMessage;

// TypeAMessage 实现 Message 接口
public class CreateContainerMessage implements IMessage {
    private final String messageContent;

    public CreateContainerMessage(String messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public String getContainerId() {
        return messageContent;
    }
}
