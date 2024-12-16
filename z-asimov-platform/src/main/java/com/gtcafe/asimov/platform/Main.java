package com.gtcafe.asimov.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.gtcafe.asimov.platform.capacity.ICapacityUnit;
import com.gtcafe.asimov.platform.capacity.impl.ReentrantCapacityUnit;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.gtcafe.asimov.platform",
        "com.gtcafe.asimov.core"
})

public class Main {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

    // @Bean
    // public ICapacityUnit capacityUnit() {
    //     return new ReentrantCapacityUnit();
    //     // return new NormalCapacityUnit();
    // }

}
