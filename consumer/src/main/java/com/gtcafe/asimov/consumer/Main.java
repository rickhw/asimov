package com.gtcafe.asimov.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.gtcafe.asimov.consumer", 
    "com.gtcafe.asimov.framework", 
    "com.gtcafe.asimov.infrastructure",
    "com.gtcafe.asimov.system"
})
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
