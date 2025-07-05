package com.gtcafe.asimov.consumer.domain.container;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "asimov.container")
@Data
public class ContainerConfig implements ApplicationRunner {

    private Integer coreSize = 16;
    private Integer maxSize = 32;
    private Integer queueCapacity = 128;
    private String threadNamePrefix = "async-hell-";
    private String threadPoolName = "helloThreadPool";

    private String hostname = "localhost:2375";
    private Integer pollIntervalMs = 5000;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // System.out.println("asimov.container:");
        // System.out.printf("  - core-size: [%s]\n", coreSize);
        // System.out.printf("  - max-size: [%s]\n", maxSize);
        // System.out.printf("  - queue-capacity: [%s]\n", queueCapacity);
        // System.out.printf("  - thread-name-prefix: [%s]\n", threadNamePrefix);
        // System.out.println();
    }

    // @Bean(name = HelloConstants.THREAD_POOL_EXECUTOR_BEANNAME)
    // public ThreadPoolTaskExecutor asyncExecutor(MeterRegistry meterRegistry) {
    //     ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //     executor.setCorePoolSize(getCoreSize());
    //     executor.setMaxPoolSize(getMaxSize());
    //     executor.setQueueCapacity(getQueueCapacity());
    //     executor.setThreadNamePrefix(getThreadNamePrefix());
    //     executor.initialize();

    //     return executor;
    // }
}
