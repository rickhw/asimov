package com.gtcafe.asimov.system.task.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class Task<T> {

    @Getter @Setter
    @Schema(description = "Task ID with UUID", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Getter @Setter
    @Schema(description = "Task State", example = "RUNNING")
    private TaskState state;

    @Getter @Setter
    @JsonProperty("_creationTime")
    private String creationTime;

    @Getter @Setter
    @JsonProperty("_finishedAt")
    private String finishedAt;

    @Getter @Setter
    private T data;

    public Task() {
        this.state = TaskState.PENDING;
        this.id = java.util.UUID.randomUUID().toString();
    }

}
