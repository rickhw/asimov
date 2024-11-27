package com.gtcafe.asimov.core.system.event;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gtcafe.asimov.core.common.utils.TimeUtils;
import com.gtcafe.asimov.core.platform.task.TaskState;

import lombok.Getter;
import lombok.Setter;

public class Event<T> {

    @Getter @Setter
    private String id;

    @Getter @Setter
    private TaskState state;

    @Getter @Setter
    @JsonProperty("_creationTime")
    private String creationTime;

    @Getter @Setter
    @JsonProperty("_lastModified")
    private String lastModified;

    @Getter @Setter
    private T data;

    public Event() {
        this.id = UUID.randomUUID().toString();
        this.state = TaskState.PENDING;
        this.creationTime = TimeUtils.timeIso8601(new Date());
        this.lastModified = TimeUtils.timeIso8601(new Date());
    }

    public void updateLastModified() {
        this.lastModified = TimeUtils.timeIso8601(new Date());
    }

    public void transit(TaskState toState) {
        this.state = toState;
        updateLastModified();
    }
}
