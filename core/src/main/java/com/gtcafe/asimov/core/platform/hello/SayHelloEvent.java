package com.gtcafe.asimov.core.platform.hello;

import java.util.UUID;

import com.gtcafe.asimov.core.domain.event.EventType;
import com.gtcafe.asimov.core.system.task.TaskDomainObject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class SayHelloEvent  {
    @Getter @Setter
    private String id;

    @Getter @Setter
    private EventType eventType;

    @Getter @Setter
    private TaskDomainObject task;

    @Getter @Setter
    private SayHelloMessage data;

    public SayHelloEvent(String message) {
        this.id = UUID.randomUUID().toString();
        this.task = new TaskDomainObject();
        this.eventType = EventType.SAY_HELLO;
        this.data = new SayHelloMessage(message);
    }

    public SayHelloEvent() {
        this.id = UUID.randomUUID().toString();
        this.task = new TaskDomainObject();
        this.eventType = EventType.SAY_HELLO;
        this.data = new SayHelloMessage();
    }

}