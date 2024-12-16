package com.gtcafe.asimov.core.system.event3;

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
