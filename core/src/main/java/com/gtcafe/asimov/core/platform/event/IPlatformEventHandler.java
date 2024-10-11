package com.gtcafe.asimov.core.platform.event;

public interface IPlatformEventHandler<T extends IPlatformMessage> {
    void handle(T message);
}
