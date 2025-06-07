package com.gtcafe.asimov;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
// @ComponentScan(basePackages = {
//         "com.gtcafe.asimov"
// })
@EnableAsync
public class Main {

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(Main.class)
            .properties("spring.config.additional-location=classpath:/asimov.yaml")
            .run(args);
    }
}
