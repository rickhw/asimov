package com.gtcafe.asimov.system.task.schema;

import lombok.Getter;

public enum ExecMode {
    ASYNC("async"),
    SYNC("sync")
    ;

    @Getter
    private String value;

    ExecMode(String value) {
        this.value = value;
    }

}
