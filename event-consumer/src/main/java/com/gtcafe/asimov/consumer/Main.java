package com.gtcafe.asimov.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.gtcafe.asimov.consumer",
    "com.gtcafe.asimov.core.utils"
})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
