package com.gtcafe.asimov.system.queue.model;

public interface EventHandler<T> {
    
    void handleEvent(T event);
}
