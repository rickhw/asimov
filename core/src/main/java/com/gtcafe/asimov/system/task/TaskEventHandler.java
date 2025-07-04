package com.gtcafe.asimov.system.task;

public interface TaskEventHandler<T> {
    
    boolean handleEvent(T event);
}
