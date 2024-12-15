package com.gtcafe.asimov.platform.hello.rest.response;

import java.util.Date;

import com.gtcafe.asimov.platform.hello.domain.Hello;

import lombok.Getter;
import lombok.Setter;

public class SayHelloResponse {

    @Getter @Setter
    private Hello message;

    @Getter
    private String launchTime;

    public SayHelloResponse(Hello message) {
        this.message = message;
        this.launchTime = new Date().toString();
    }
}
