package com.gtcafe.asimov.system.task.schema;

import lombok.Getter;

public enum TaskState {
    CREATING("Creating"),
    PENDING("Pending"),
    RUNNING("Running"),
    COMPLETED("Completed"),
    FAILURE("Failure")
    
    ;

    @Getter
    private String value;

    TaskState(String value) {
        this.value = value;
    }

}
