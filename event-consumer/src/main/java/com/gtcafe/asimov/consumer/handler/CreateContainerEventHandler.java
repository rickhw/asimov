package com.gtcafe.asimov.consumer.handler;

import com.gtcafe.asimov.consumer.IEventHandler;

// TypeAEventHandler 处理 TypeAMessage
@Service
public class CreateContainerEventHandler implements IEventHandler<TypeAMessage> {
    @Override
    public void handle(TypeAMessage message) {
        System.out.println("Handling Type A Message: " + message.getMessageContent());
    }
}