package com.gtcafe.asimov.consumer.handler;

import com.gtcafe.asimov.core.event.IMessage;

public interface IEventHandler<T extends IMessage> {
    void handle(T message);
}
