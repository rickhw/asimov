package com.gtcafe.asimov.platform.hello.model;

import com.gtcafe.asimov.system.queue.event.Event;

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