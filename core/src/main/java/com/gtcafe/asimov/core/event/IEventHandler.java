package com.gtcafe.asimov.core.event;

public interface IEventHandler<T extends IMessage> {
    void handle(T message);
}
