package com.gtcafe.asimov.platform.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.platform.hello.model.Hello;
import com.gtcafe.asimov.platform.hello.rest.request.SayHelloRequest;
import com.gtcafe.asimov.system.utils.TimeUtils;

@Service
public class HelloMapper {
    
    @Autowired
    private TimeUtils timeUtils;

    public Hello mapRequestToDomain(SayHelloRequest request) {
        // Hello hello = new Hello();
        // hello.setMessage(request.getMessage());
        // hello.setTimestamp(timeUtils.currentTimeIso8601());

        return Hello.builder()
            .message(request.getMessage())
            // .timestamp(timeUtils.currentTimeIso8601())
            .build();
    }
}
