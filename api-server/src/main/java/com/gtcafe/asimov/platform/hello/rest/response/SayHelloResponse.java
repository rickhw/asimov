package com.gtcafe.asimov.platform.hello.rest.response;

import com.gtcafe.asimov.platform.hello.model.Hello;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class SayHelloResponse {

    @Getter @Setter
    private Hello message;

    @Getter @Setter
    private String launchTime;

}
