package com.gtcafe.asimov.core.event;

import com.gtcafe.asimov.core.event.IMessage;

public interface IEventHandler<T extends IMessage> {
    void handle(T message);
}
