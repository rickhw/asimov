package com.gtcafe.asimov.core.domain.event;

import java.util.UUID;

public class Event<T extends IMessage>  {
    private final String id;
    private final EventType eventType;
    private final T data;

    public Event(String id, EventType eventType, T data) {
        this.id = id;
        this.eventType = eventType;
        this.data = data;
    }

    public Event(EventType eventType, T data) {
        this.id = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.data = data;
    }

    public EventType getEventType() {
        return eventType;
    }
    
    public String getEventId() { return this.id; }

    public T getData() {
        return data;
    }
}