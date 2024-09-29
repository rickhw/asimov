package com.gtcafe.asimov.core.event.message;


import com.gtcafe.asimov.core.event.Message;

public class DeleteContainerMessage implements Message {
    private final String messageContent;

    public DeleteContainerMessage(String messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public String getMessageContent() {
        return messageContent;
    }
}
