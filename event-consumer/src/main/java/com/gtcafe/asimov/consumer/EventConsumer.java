package com.gtcafe.asimov.consumer;


import com.example.common.event.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EventConsumer {

    private final IEventHandler<TypeAMessage> typeAHandler;
    private final IEventHandler<TypeBMessage> typeBHandler;

    public EventConsumer(IEventHandler<TypeAMessage> typeAHandler, IEventHandler<TypeBMessage> typeBHandler) {
        this.typeAHandler = typeAHandler;
        this.typeBHandler = typeBHandler;
    }

    // RabbitMQ 消费者
    @RabbitListener(queues = "eventQueue")
    public void receiveEvent(Event<? extends Message> event) {
        switch (event.getEventType()) {
            case TYPE_A -> typeAHandler.handle((TypeAMessage) event.getData());
            case TYPE_B -> typeBHandler.handle((TypeBMessage) event.getData());
            default -> throw new IllegalArgumentException("Unknown event type: " + event.getEventType());
        }
    }
}