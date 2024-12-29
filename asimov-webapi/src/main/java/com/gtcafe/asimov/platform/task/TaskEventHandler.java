package com.gtcafe.asimov.platform.task;

public interface TaskEventHandler<T> {
    
    void handleEvent(T event);
}
