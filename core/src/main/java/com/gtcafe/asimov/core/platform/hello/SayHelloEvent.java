package com.gtcafe.asimov.core.platform.hello;

import com.gtcafe.asimov.core.domain.event.Event;
import com.gtcafe.asimov.core.domain.event.EventType;

public class SayHelloEvent extends Event <SayHelloMessage> {
    public SayHelloEvent(SayHelloMessage data) {
        super(EventType.SAY_HELLO, data);
    }    
}

