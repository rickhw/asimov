package com.gtcafe.asimov.system.tenant.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class Metadata {

    @Getter @Setter
    @JsonProperty("_state")
    State state;		// "pending"

    @Getter @Setter
    @JsonProperty("_creationTime")
    String creationTime;	// "2021-12-10T00:29:06.800+08:00",

    @Getter @Setter
    @JsonProperty("_lastModified")
    String lastModified;	// "2021-12-10T00:29:06.800+08:00"

    Metadata() {
        this.state = State.PENDING;
    }

}