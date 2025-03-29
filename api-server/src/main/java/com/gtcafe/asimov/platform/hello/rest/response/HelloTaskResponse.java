package com.gtcafe.asimov.platform.hello.rest.response;

import com.gtcafe.asimov.platform.hello.model.Hello;
import com.gtcafe.asimov.platform.task.schema.Task;

public class HelloTaskResponse extends Task<Hello> {

    public HelloTaskResponse(Hello hello) {
        super();
        this.setData(hello);
    }

    public HelloTaskResponse() {
        super();
    }
}
