package com.gtcafe.asimov.consumer.domain.container.handler;

import com.gtcafe.asimov.core.domain.container.DeleteContainerMessage;
import com.gtcafe.asimov.core.domain.event.IEventHandler;

import org.springframework.stereotype.Service;

@Service
public class DeleteContainerEventHandler implements IEventHandler<DeleteContainerMessage> {

    @Override
    public void handle(DeleteContainerMessage message) {
        System.out.println("Handling Type A Message: " + message.getContent());
    }
}