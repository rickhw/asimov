package com.gtcafe.asimov.consumer.system.hello.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.gtcafe.asimov.system.hello.HelloConstants;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "asimov.system.hello.consumer.async-thread-pool")
@Data
public class HelloConsumerAsyncConfig implements ApplicationRunner {

    private Integer coreSize = 16;
    private Integer maxSize = 32;
    private Integer queueCapacity = 128;
    private String threadNamePrefix = "async-hell-";
    private String threadPoolName = "helloThreadPool";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("app.hello.consumer.async-thread-pool:");
        System.out.printf("  - core-size: [%s]\n", coreSize);
        System.out.printf("  - max-size: [%s]\n", maxSize);
        System.out.printf("  - queue-capacity: [%s]\n", queueCapacity);
        System.out.printf("  - thread-name-prefix: [%s]\n", threadNamePrefix);
        System.out.println();
    }

    @Bean(name = HelloConstants.THREAD_POOL_EXECUTOR_BEANNAME)
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(getCoreSize());
        executor.setMaxPoolSize(getMaxSize());
        executor.setQueueCapacity(getQueueCapacity());
        executor.setThreadNamePrefix(getThreadNamePrefix());
        executor.initialize();

        return executor;
    }
}
