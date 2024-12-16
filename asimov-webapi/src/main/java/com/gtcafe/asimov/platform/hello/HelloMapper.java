package com.gtcafe.asimov.platform.hello;

import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.platform.hello.Hello;
import com.gtcafe.asimov.platform.hello.rest.request.SayHelloRequest;

@Service
public class HelloMapper {
    
    public Hello mapRequestToDomain(SayHelloRequest request) {
        Hello hello = new Hello();
        hello.setMessage(request.getMessage());
        return hello;
    }
}
