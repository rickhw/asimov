package com.gtcafe.asimov.system.hello.model;

import com.gtcafe.asimov.system.task.schema.Task;

public class HelloEvent extends Task<Hello> {
    public HelloEvent(Hello data) {
        super();
        setData(data);
    }

    // needed by de-serialization
    public HelloEvent() {
        super();
    }
}