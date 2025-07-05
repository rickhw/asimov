package com.gtcafe.asimov.system.hello;

import org.springframework.stereotype.Service;

import com.gtcafe.asimov.rest.domain.hello.request.SayHelloRequest;
import com.gtcafe.asimov.rest.domain.hello.response.HelloTaskResponse;
import com.gtcafe.asimov.system.hello.model.Hello;
import com.gtcafe.asimov.system.hello.model.HelloEvent;

@Service
public class HelloMapper {


    public Hello mapRequestToDomain(SayHelloRequest request) {
        // Hello hello = new Hello();
        // hello.setMessage(request.getMessage());
        // hello.setTimestamp(timeUtils.currentTimeIso8601());

        Hello obj = new Hello();
        obj.setMessage(request.getMessage());
        
        return obj;
    }

    public HelloTaskResponse mapHelloEventToResponse(HelloEvent event) {
        HelloTaskResponse res = new HelloTaskResponse();
        res.setId(event.getId());
        res.setCreationTime(event.getCreationTime());
        res.setState(event.getState());
        res.setData(event.getData());
        res.setCreationTime(event.getCreationTime());
        res.setFinishedAt(event.getFinishedAt());

        return res;
    }
}
