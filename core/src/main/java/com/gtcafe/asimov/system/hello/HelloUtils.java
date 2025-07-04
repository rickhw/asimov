package com.gtcafe.asimov.system.hello;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import com.gtcafe.asimov.framework.constants.KindConstants;

// @Configuration
// @ConfigurationProperties(prefix = "asimov.system.hello")
// @Data
public class HelloUtils { // implements ApplicationRunner {


    // @Value("asimov.system.hello.queues.task-queue.name:asimov.platform.hello")
    // private String taskQueneName;

    // @Bean(name = "helloThreadPool")
    // public ThreadPoolTaskExecutor asyncExecutor(MeterRegistry meterRegistry) {
    //     ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //     executor.setCorePoolSize(getCoreSize());
    //     executor.setMaxPoolSize(getMaxSize());
    //     executor.setQueueCapacity(getQueueCapacity());
    //     executor.setThreadNamePrefix(getThreadNamePrefix());
    //     executor.initialize();
        
    //     // 註冊 metrics
    //     // registerExecutorMetrics(executor, "helloExecutor", meterRegistry);

    //     return executor;
    // }

    // @Override
    // public void run(ApplicationArguments args) throws Exception {
    //     System.out.println("asimov.system.hello:");
    //     // System.out.printf("  - core-size: [%s]\n", coreSize);
    //     // System.out.printf("  - max-size: [%s]\n", maxSize);
    //     // System.out.printf("  - queue-capacity: [%s]\n", queueCapacity);
    //     // System.out.printf("  - thread-name-prefix: [%s]\n", threadNamePrefix);
    //     System.out.println();
    // }

    public static String renderCacheKey(String id) {
        return String.format("%s:%s", KindConstants.PLATFORM_HELLO, id);
    }
    
}
