package com.gtcafe.asimov.core.domain.event;

public interface IEventHandler<T extends IMessage> {
    void handle(T message);
}
