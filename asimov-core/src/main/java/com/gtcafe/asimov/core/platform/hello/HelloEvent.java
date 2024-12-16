package com.gtcafe.asimov.core.platform.hello;

import com.gtcafe.asimov.core.system.event.Event;

import lombok.ToString;

@ToString
public class HelloEvent extends Event<Hello> {
    public HelloEvent(Hello data) {
        super(data);
    }
}