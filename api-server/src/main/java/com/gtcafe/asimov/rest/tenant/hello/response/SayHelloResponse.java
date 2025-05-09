package com.gtcafe.asimov.rest.tenant.hello.response;

import com.gtcafe.asimov.system.hello.model.Hello;

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
