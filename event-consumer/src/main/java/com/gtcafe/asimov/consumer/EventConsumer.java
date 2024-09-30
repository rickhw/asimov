package com.gtcafe.asimov.consumer.handler;

import com.gtcafe.asimov.core.event.Event;
import com.gtcafe.asimov.core.event.EventType;
import com.gtcafe.asimov.core.event.IMessage;
import com.gtcafe.asimov.core.event.message.CreateContainerMessage;
import com.gtcafe.asimov.core.event.message.DeleteContainerMessage;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EventConsumer {

    private final IEventHandler<CreateContainerMessage> createContainerHandler;
    private final IEventHandler<DeleteContainerMessage> deleteContainerHandler;

    public EventConsumer(IEventHandler<CreateContainerMessage> createContainerHandler, IEventHandler<DeleteContainerMessage> deleteContainerHandler) {
        this.createContainerHandler = createContainerHandler;
        this.deleteContainerHandler = deleteContainerHandler;
    }

    @RabbitListener(queues = "eventQueue")
    public void receiveEvent(Event<? extends IMessage> event) {
        switch (event.getEventType()) {
            case CREATE_CONTAINER -> createContainerHandler.handle((CreateContainerMessage) event.getData());
            case DELETE_CONTAINER -> deleteContainerHandler.handle((DeleteContainerMessage) event.getData());
            default -> throw new IllegalArgumentException("Unknown event type: " + event.getEventType());
        }
    }
}