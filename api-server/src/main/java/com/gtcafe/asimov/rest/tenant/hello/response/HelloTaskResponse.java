package com.gtcafe.asimov.rest.tenant.hello.response;

import com.gtcafe.asimov.system.hello.model.Hello;
import com.gtcafe.asimov.system.task.schema.Task;

public class HelloTaskResponse extends Task<Hello> {

    public HelloTaskResponse(Hello hello) {
        super();
        this.setData(hello);
    }

    public HelloTaskResponse() {
        super();
    }
}
