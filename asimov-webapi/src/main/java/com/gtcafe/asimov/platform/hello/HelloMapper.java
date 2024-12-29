package com.gtcafe.asimov.platform.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.platform.hello.consumer.HelloEvent;
import com.gtcafe.asimov.platform.hello.model.Hello;
import com.gtcafe.asimov.platform.hello.rest.request.SayHelloRequest;
import com.gtcafe.asimov.platform.hello.rest.response.HelloTaskResponse;
import com.gtcafe.asimov.system.utils.TimeUtils;

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
