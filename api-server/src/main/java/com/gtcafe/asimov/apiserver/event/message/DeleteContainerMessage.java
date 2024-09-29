package com.gtcafe.asimov.apiserver.event.message;


import com.gtcafe.asimov.apiserver.event.IMessage;

public class DeleteContainerMessage implements IMessage {
    private final String containerId;

    public DeleteContainerMessage(String containerId) {
        this.containerId = containerId;
    }

    @Override
    public String getContainerId() {
        return containerId;
    }
}
