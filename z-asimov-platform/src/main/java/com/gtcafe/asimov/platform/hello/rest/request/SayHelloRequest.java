package com.gtcafe.asimov.platform.hello.rest.request;

import com.gtcafe.asimov.platform.hello.domain.Hello;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class SayHelloRequest {

    @NotBlank(message = "message cannot empty")
    @Getter @Setter
    private Hello hello;

}
