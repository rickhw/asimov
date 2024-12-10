package com.gtcafe.asimov.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.gtcafe.asimov.platform",
    "com.gtcafe.asimov.core"
})

public class Main {
   
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
}
