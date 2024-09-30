package com.gtcafe.asimov.consumer.handler;

import com.gtcafe.asimov.core.event.message.CreateContainerMessage;
import org.springframework.stereotype.Service;

@Service
public class CreateContainerEventHandler implements IEventHandler<CreateContainerMessage> {
    @Override
    public void handle(CreateContainerMessage message) {
        System.out.println("Handling Type A Message: " + message.getContent());
    }
}