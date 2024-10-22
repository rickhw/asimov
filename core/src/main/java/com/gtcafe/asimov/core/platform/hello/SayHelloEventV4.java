package com.gtcafe.asimov.core.platform.hello;

import com.gtcafe.asimov.core.system.task.AbstractTask;
import com.gtcafe.asimov.core.system.task.TaskState;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class SayHelloEventV4 extends AbstractTask  {

    @Getter @Setter
    private SayHelloMessage data;

    public SayHelloEventV4() {
        super();
        this.data = new SayHelloMessage();
    }

    public SayHelloEventV4(String message) {
        super();
        this.data = new SayHelloMessage(message);
    }

    public void transit(TaskState toState) {
        setState(toState);
        updateLastModified();
    }
}