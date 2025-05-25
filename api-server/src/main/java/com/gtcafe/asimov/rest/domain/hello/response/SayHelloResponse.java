package com.gtcafe.asimov.rest.domain.hello.response;

import com.gtcafe.asimov.system.hello.model.Hello;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SayHelloResponse {

    private Hello message;
    private String launchTime;

}
