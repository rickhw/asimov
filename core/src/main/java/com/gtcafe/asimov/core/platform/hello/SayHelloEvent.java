package com.gtcafe.asimov.core.platform.hello;

import com.gtcafe.asimov.core.system.task.AbstractTask;
import com.gtcafe.asimov.core.system.task.TaskState;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class SayHelloEvent extends AbstractTask  {

    @Getter @Setter
    private SayHelloMessage data;

    public SayHelloEvent() {
        super();
        this.data = new SayHelloMessage();
    }

    public SayHelloEvent(String message) {
        super();
        this.data = new SayHelloMessage(message);
    }

    public void transit(TaskState toState) {
        setState(toState);
        updateLastModified();
    }
}