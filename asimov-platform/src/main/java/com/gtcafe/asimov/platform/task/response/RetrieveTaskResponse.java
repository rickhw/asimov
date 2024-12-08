package com.gtcafe.asimov.platform.task.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gtcafe.asimov.core.platform.hello.SayHelloEvent;
import com.gtcafe.asimov.core.platform.tenant.RegisterTenantEvent;

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


    @Getter @Setter
    private Object data;

    public RetrieveTaskResponse() {}

    // TODO: convert as generic
    public RetrieveTaskResponse(RegisterTenantEvent tdo) {
        this.id = tdo.getId();
        this.state = tdo.getState().toString();
        this.data = tdo.getData();
        this.creationTime = tdo.getCreationTime();
        this.lastModified = tdo.getLastModified();
    }

    public RetrieveTaskResponse(SayHelloEvent tdo) {
        this.id = tdo.getId();
        this.state = tdo.getState().toString();
        this.data = tdo.getData();
        this.creationTime = tdo.getCreationTime();
        this.lastModified = tdo.getLastModified();
    }
}
