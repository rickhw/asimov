package com.gtcafe.asimov.system.task;

public interface TaskEventHandler<T> {
    
    void handleEvent(T event);
}
