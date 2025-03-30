package com.gtcafe.asimov.system.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.framework.utils.TimeUtils;
import com.gtcafe.asimov.system.hello.consumer.HelloEvent;
import com.gtcafe.asimov.system.hello.model.Hello;
import com.gtcafe.asimov.system.hello.rest.request.SayHelloRequest;
import com.gtcafe.asimov.system.hello.rest.response.HelloTaskResponse;

@Service
public class HelloMapper {
    
    @Autowired
    private TimeUtils timeUtils;

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
