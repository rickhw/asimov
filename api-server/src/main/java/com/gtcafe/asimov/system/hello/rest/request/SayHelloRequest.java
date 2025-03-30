package com.gtcafe.asimov.system.hello.rest.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class SayHelloRequest {

    @NotBlank(message = "message cannot empty")
    @Getter @Setter
    private String message;

}
