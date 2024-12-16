package com.gtcafe.asimov.platform.tenant.domain.model;

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