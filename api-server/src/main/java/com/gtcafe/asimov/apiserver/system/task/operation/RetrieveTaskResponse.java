package com.gtcafe.asimov.apiserver.system.task.operation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gtcafe.asimov.core.platform.hello.SayHelloEventV4;

import lombok.Getter;
import lombok.Setter;


public class RetrieveTaskResponse {

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

    // private String kind;

    // private String operationId;

    @Getter @Setter
    private Object data;

    public RetrieveTaskResponse() {}

    // public RetrieveTaskResponse(TaskDomainObject tdo) {
    public RetrieveTaskResponse(SayHelloEventV4 tdo) {
        this.id = tdo.getId();
        this.state = tdo.getState().toString();
        this.data = tdo.getData();
        this.creationTime = tdo.getCreationTime();
        this.lastModified = tdo.getLastModified();
    }

}
