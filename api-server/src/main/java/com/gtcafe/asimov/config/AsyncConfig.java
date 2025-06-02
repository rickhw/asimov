package com.gtcafe.asimov.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.gtcafe.asimov.system.hello.HelloConfig;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Autowired
    private HelloConfig helloConfig;

    @Bean(name = "helloThreadPool")
    public ThreadPoolTaskExecutor asyncExecutor(MeterRegistry meterRegistry) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(helloConfig.getCoreSize());
        executor.setMaxPoolSize(helloConfig.getMaxSize());
        executor.setQueueCapacity(helloConfig.getQueueCapacity());
        executor.setThreadNamePrefix(helloConfig.getThreadNamePrefix());
        executor.initialize();
        
        // 註冊 metrics
        registerExecutorMetrics(executor, "helloExecutor", meterRegistry);

        return executor;
    }


    private void registerExecutorMetrics(ThreadPoolTaskExecutor executor, String name, MeterRegistry registry) {
        ThreadPoolExecutor threadPool = executor.getThreadPoolExecutor();

        Gauge.builder("executor.pool.size", threadPool, ThreadPoolExecutor::getPoolSize)
                .description("The current number of threads in the pool")
                .tag("name", name)
                .register(registry);

        Gauge.builder("executor.active.count", threadPool, ThreadPoolExecutor::getActiveCount)
                .description("The approximate number of threads that are actively executing tasks")
                .tag("name", name)
                .register(registry);

        Gauge.builder("executor.queue.size", threadPool, e -> e.getQueue().size())
                .description("The number of tasks currently in the queue")
                .tag("name", name)
                .register(registry);

        Gauge.builder("executor.completed.count", threadPool, e -> e.getCompletedTaskCount())
                .description("The number of completed tasks")
                .tag("name", name)
                .register(registry);
    }
}
