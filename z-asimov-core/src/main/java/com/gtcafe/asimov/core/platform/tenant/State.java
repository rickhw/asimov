package com.gtcafe.asimov.core.platform.tenant;

import lombok.Getter;

public enum State {
    PENDING("pending"),
    RUNNING("running"),
    STARTING("starting"),
    TERMINATED("terminated")
    ;

    @Getter
    private String value;

    State(String value) {
        this.value = value;
    }

}