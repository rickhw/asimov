package com.gtcafe.asimov.core.domain.event;

import java.util.UUID;

import com.gtcafe.asimov.core.system.task.TaskDomainObject;

public class Event<T extends IMessage>  {
    private final String id;
    private final EventType eventType;
    private final TaskDomainObject task;
    private final T data;

    public Event(String id, EventType eventType, T data) {
        this.id = id;
        this.eventType = eventType;
        this.task = new TaskDomainObject();
        this.data = data;
    }

    public Event(EventType eventType, T data) {
        this.id = UUID.randomUUID().toString();
        this.task = new TaskDomainObject();
        this.eventType = eventType;
        this.data = data;
    }

    public EventType getEventType() {
        return eventType;
    }
    
    public String getEventId() { return this.id; }

    public TaskDomainObject getTask() {
        return this.task;
    }

    public T getData() {
        return data;
    }
}