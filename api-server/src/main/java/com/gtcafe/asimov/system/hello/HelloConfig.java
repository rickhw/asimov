package com.gtcafe.asimov.system.hello;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "asimov.system.hello.consumer.async-thread-pool")
@Data
public class HelloConfig implements ApplicationRunner {

    private Integer coreSize;
    private Integer maxSize;
    private Integer queueCapacity;
    private String threadNamePrefix;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("app.hello.consumer.async-thread-pool:");
        System.out.printf("  - core-size: [%s]\n", coreSize);
        System.out.printf("  - max-size: [%s]\n", maxSize);
        System.out.printf("  - queue-capacity: [%s]\n", queueCapacity);
        System.out.printf("  - thread-name-prefix: [%s]\n", threadNamePrefix);
        System.out.println();
    }

    
}
