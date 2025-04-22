package com.gtcafe.asimov.rest.tenant.task.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class RetrieveTaskResponse<T> {

    @Getter @Setter
    private String id;

    @Getter @Setter
    private String state;

    @Getter @Setter
    @JsonProperty("_creationTime")
    private String creationTime;

    @Getter @Setter
    @JsonProperty("_lastModified")
    private String lastModified;

    @Getter @Setter
    private T data;

}
