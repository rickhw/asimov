package com.gtcafe.asimov.consumer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.gtcafe.asimov.framework", 
    "com.gtcafe.asimov.infrastructure",
    "com.gtcafe.asimov.system",
    "com.gtcafe.asimov.consumer"
})
@EnableAsync
public class Main {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Main.class)
            .properties("spring.config.additional-location=classpath:/asimov.yaml")
            .run(args);
    }
}
