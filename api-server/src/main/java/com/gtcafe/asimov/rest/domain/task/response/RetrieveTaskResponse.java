package com.gtcafe.asimov.rest.domain.task.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RetrieveTaskResponse<T> {

    private String id;

    private String state;

    @JsonProperty("_creationTime")
    private String creationTime;

    @JsonProperty("_lastModified")
    private String lastModified;

    private T data;

}
