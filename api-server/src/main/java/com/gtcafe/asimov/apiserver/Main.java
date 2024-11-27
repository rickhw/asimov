package com.gtcafe.asimov.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.gtcafe.asimov.apiserver",
    "com.gtcafe.asimov.core",
    "com.gtcafe.asimov.core.common"
})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}