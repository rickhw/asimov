package com.gtcafe.asimov.platform.hello.rest.response;

import com.gtcafe.asimov.core.platform.hello.Hello;
import com.gtcafe.asimov.platform.task.domain.schema.Task;

public class HelloTaskResponse extends Task<Hello> {

    public HelloTaskResponse(Hello hello) {
        super();
        this.setData(hello);
    }
}
