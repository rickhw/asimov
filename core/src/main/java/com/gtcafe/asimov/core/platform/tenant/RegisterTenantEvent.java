package com.gtcafe.asimov.core.platform.tenant;

import com.gtcafe.asimov.core.system.task.AbstractTask;
import com.gtcafe.asimov.core.system.task.TaskState;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class RegisterTenantEvent extends AbstractTask  {

    @Getter @Setter
    private RegisterTenantMessage data;

    public RegisterTenantEvent() {
        super();
        this.data = new RegisterTenantMessage();
    }

    public RegisterTenantEvent(String message) {
        super();
        this.data = new RegisterTenantMessage(message);
    }

    public void transit(TaskState toState) {
        setState(toState);
        updateLastModified();
    }
}