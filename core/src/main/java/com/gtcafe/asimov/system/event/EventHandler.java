package com.gtcafe.asimov.system.event;

public interface EventHandler<T extends IMessage> {
    void handleEvent(Event<T> event);
}