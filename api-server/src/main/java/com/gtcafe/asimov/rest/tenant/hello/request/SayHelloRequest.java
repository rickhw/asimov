package com.gtcafe.asimov.rest.tenant.hello.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class SayHelloRequest {

    @NotBlank(message = "message cannot empty")
    @Getter @Setter
    private String message;

}
