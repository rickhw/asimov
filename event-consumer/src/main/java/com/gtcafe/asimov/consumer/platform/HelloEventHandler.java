package com.gtcafe.asimov.consumer.platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.platform.hello.SayHelloMessage;
import com.gtcafe.asimov.core.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HelloEventHandler {

    @Autowired
    private JsonUtils _jsonUtils;

    public void sayHello(SayHelloMessage message) {

        try {
            Thread.sleep(10000);
            System.out.println("message: " + message.getData());

        } catch (Exception ex) {

        }

    }
}