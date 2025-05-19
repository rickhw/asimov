package com.gtcafe.asimov.system.tenant.model;

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