package com.gtcafe.asimov.core.system.event2;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gtcafe.asimov.core.common.utils.TimeUtils;

import lombok.Getter;
import lombok.Setter;

// public class Metadata<T> {
public class Metadata {

    @Getter @Setter
    @JsonProperty("_id")
    private String id;

    @Getter @Setter
    //private T state;
    private Enum<?> state;

    @Getter @Setter
    @JsonProperty("_creationTime")
    private String creationTime;

    @Getter @Setter
    @JsonProperty("_lastModified")
    private String lastModified;

    public Metadata() {
        this.id = UUID.randomUUID().toString();
        this.creationTime = TimeUtils.timeIso8601(new Date());
        this.lastModified = TimeUtils.timeIso8601(new Date());
    }

    public void updateLastModified() {
        this.lastModified = TimeUtils.timeIso8601(new Date());
    }

    public void transit(Enum<?> toState) {
        this.state = toState;
        updateLastModified();
    }
}
