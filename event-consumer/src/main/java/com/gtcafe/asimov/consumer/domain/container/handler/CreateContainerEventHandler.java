package com.gtcafe.asimov.consumer.domain.container.handler;

import com.gtcafe.asimov.core.domain.container.CreateContainerMessage;
import com.gtcafe.asimov.core.domain.event.IEventHandler;

import org.springframework.stereotype.Service;

@Service
public class CreateContainerEventHandler implements IEventHandler<CreateContainerMessage> {
    @Override
    public void handle(CreateContainerMessage message) {
        System.out.println("Handling Type A Message: " + message.getData());
    }
}