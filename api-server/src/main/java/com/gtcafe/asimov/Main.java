package com.gtcafe.asimov;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
// @ComponentScan(basePackages = {
//         "com.gtcafe.asimov"
// })
public class Main {

    public static void main(String[] args) throws Exception {
        // SpringApplication.run(Main.class, args);
        new SpringApplicationBuilder(Main.class)
            .properties("spring.config.additional-location=classpath:/asimov.yaml")
            .run(args);
    }
}
