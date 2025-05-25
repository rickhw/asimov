package com.gtcafe.asimov.rest.domain.hello.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SayHelloRequest {

    @NotBlank(message = "message cannot empty")
    private String message;

}
