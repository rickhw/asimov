package com.gtcafe.asimov.platform.task.domain.schema;

import lombok.Getter;

public enum TaskState {
    CREATING("Creating"),
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
