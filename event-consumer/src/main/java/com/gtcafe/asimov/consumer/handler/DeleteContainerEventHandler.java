package com.gtcafe.asimov.consumer.handler;

import com.gtcafe.asimov.core.event.message.DeleteContainerMessage;
import org.springframework.stereotype.Service;

@Service
public class DeleteContainerEventHandler implements IEventHandler<DeleteContainerMessage> {
    
    @Override
    public void handle(DeleteContainerMessage message) {
        System.out.println("Handling Type A Message: " + message.getContent());
    }
}