package com.gtcafe.asimov.core.system.event2;

public interface EventHandlerV2<T extends IMessageV2> {
    void handleEvent(EventV2<T> event);
}