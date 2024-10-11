package com.gtcafe.asimov.core.platform.event;

import java.util.UUID;

public class PlatformEvent<T extends IPlatformMessage>  {
    private final String id;
    private final PlatformEventType eventType;
    private final T data;

    public PlatformEvent(String id, PlatformEventType eventType, T data) {
        this.id = id;
        this.eventType = eventType;
        this.data = data;
    }

    public PlatformEvent(PlatformEventType eventType, T data) {
        this.id = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.data = data;
    }

    public PlatformEventType getEventType() {
        return eventType;
    }
    
    public String getEventId() { return this.id; }

    public T getData() {
        return data;
    }
}