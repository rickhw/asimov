package com.gtcafe.asimov.core.event;

public class Event<T extends Message> {
    private final EventType eventType;
    private final T data;

    public Event(EventType eventType, T data) {
        this.eventType = eventType;
        this.data = data;
    }

    public EventType getEventType() {
        return eventType;
    }

    public T getData() {
        return data;
    }
}