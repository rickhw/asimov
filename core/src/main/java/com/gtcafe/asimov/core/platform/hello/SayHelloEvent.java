package com.gtcafe.asimov.core.platform.hello;

import com.gtcafe.asimov.core.system.event.Event;

import lombok.ToString;

@ToString
public class SayHelloEvent extends Event<SayHelloMessage> {
    public SayHelloEvent() {
        super();
        setData(new SayHelloMessage());
    }

    public SayHelloEvent(String message) {
        super();
        setData(new SayHelloMessage(message));
    }
}