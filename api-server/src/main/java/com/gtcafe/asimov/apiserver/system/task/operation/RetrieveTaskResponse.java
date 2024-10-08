package com.gtcafe.asimov.apiserver.system.task.operation;

import com.gtcafe.asimov.core.system.task.TaskDomainObject;

public class RetrieveTaskResponse {

    private String id;

    // pending, running, complete, failure
    private String state;

    // private String kind;

    // private String operationId;

    private Object data;

    public RetrieveTaskResponse() {}

    public RetrieveTaskResponse(TaskDomainObject tdo) {
        this.id = tdo.getTaskId();
        this.state = tdo.getMetadata().get_state().toString();
        this.data = tdo.getSpec();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    // public String getKind() {
    //     return kind;
    // }

    // public void setKind(String kind) {
    //     this.kind = kind;
    // }

    // public String getOperationId() {
    //     return operationId;
    // }

    // public void setOperationId(String operationId) {
    //     this.operationId = operationId;
    // }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
