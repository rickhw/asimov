package com.gtcafe.asimov.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.gtcafe.asimov.system.hello.HelloConfig;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Autowired
    private HelloConfig helloConfig;

    @Bean(name = "helloThreadPool")
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(helloConfig.getCoreSize());
        executor.setMaxPoolSize(helloConfig.getMaxSize());
        executor.setQueueCapacity(helloConfig.getQueueCapacity());
        executor.setThreadNamePrefix(helloConfig.getThreadNamePrefix());
        executor.initialize();
        return executor;
    }
}
