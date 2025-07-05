package com.gtcafe.asimov.system.hello.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "asimov.system.hello.queues.task-queue")
@Data
public class HelloQueueConfig implements ApplicationRunner {

    private String queueName;
    private String exchangeName;
    private String routingKeyName;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("asimov.system.hello.queues.task-queue:");
        System.out.printf("  - queue-name: [%s]\n", queueName);
        System.out.printf("  - exchange-name: [%s]\n", exchangeName);
        System.out.printf("  - routing-key-name: [%s]\n", routingKeyName);
        System.out.println();
    }
   
}
