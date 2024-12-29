package com.gtcafe.asimov.system.queue.event;

import lombok.Getter;
import lombok.Setter;

public class Event<T> {

    @Getter @Setter
    private String eventId;

    @Getter @Setter
    private String taskId;
    
    @Getter @Setter
    private EventContext context;

    @Getter @Setter
    private T data;

    public Event(T data) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.data = data;
    }

    public Event() {
        this.eventId = java.util.UUID.randomUUID().toString();
    }

}
