package com.gtcafe.asimov.core.event.message;


import com.gtcafe.asimov.core.event.IMessage;

public class DeleteContainerMessage implements IMessage {
    private final String content;

    public DeleteContainerMessage(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}
