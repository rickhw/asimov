package com.gtcafe.asimov.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.gtcafe.asimov.apiserver", // 主應用包
    "com.gtcafe.asimov.core.utils"  // 指定 jsonUtils 包
})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}