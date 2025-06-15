package com.gtcafe.asimov.client;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Autowired
    private TaskProperties prop;

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(prop.getThreadPool().getCoreSize());
        executor.setMaxPoolSize(prop.getThreadPool().getMaxSize());
        executor.setQueueCapacity(prop.getThreadPool().getQueueCapacity());
        executor.setThreadNamePrefix(prop.getThreadPool().getThreadNamePrefix());
        executor.initialize();
        return executor;
    }
}
