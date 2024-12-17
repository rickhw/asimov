package com.gtcafe.asimov.core.platform.hello;

import com.gtcafe.asimov.core.system.event.Event;

// @ToString
// @Builder
public class HelloEvent extends Event<Hello> {
    public HelloEvent(Hello data) {
        super(data);
    }


    // public static HelloEvent create(Hello data) {
    //     return HelloEvent.builder()
    //         .data(data)
    //         .build();
    // }
}