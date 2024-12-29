package com.gtcafe.asimov.platform.hello.consumer;

import com.gtcafe.asimov.platform.hello.model.Hello;
import com.gtcafe.asimov.system.queue.model.Event;

public class HelloEvent extends Event<Hello> {
    public HelloEvent(Hello data) {
        super(data);
    }
}