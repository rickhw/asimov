package com.gtcafe.asimov.apiserver.system.task.operation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTaskRequest {

	@NotBlank(message = "kind cannot empty")
    @Size(max = 50, message = "kind max lenght is 50.")
    private String kind;

    @NotBlank(message = "operationId cannot empty")
    @Size(max = 50, message = "operationId max lenght is 50.")
    private String operationId;

    @NotBlank(message = "data cannot empty")
    @Size(max = 65536, message = "data max lenght is 65535.")
    private String data;


}
