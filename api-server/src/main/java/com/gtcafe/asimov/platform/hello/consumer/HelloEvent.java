package com.gtcafe.asimov.platform.hello.consumer;

import com.gtcafe.asimov.platform.hello.model.Hello;
import com.gtcafe.asimov.platform.task.schema.Task;

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