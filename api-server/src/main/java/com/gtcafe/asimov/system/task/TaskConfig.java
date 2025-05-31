package com.gtcafe.asimov.system.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfig {

    @Value("${asimov.task.thread-pool.size}")
    private String openapiTitle;


    
}
