package com.gtcafe.asimov.platform.hello.rest.response;

import org.springframework.beans.factory.annotation.Autowired;

import com.gtcafe.asimov.core.platform.hello.Hello;
import com.gtcafe.asimov.core.system.utils.TimeUtils;

import lombok.Getter;
import lombok.Setter;

public class SayHelloResponse {

    @Getter @Setter
    private Hello message;

    @Getter
    private String launchTime;

    @Autowired
    private TimeUtils timeUtils;

    public SayHelloResponse(Hello message) {
        this.message = message;
        this.launchTime = timeUtils.currentTimeIso8601();
    }
}
