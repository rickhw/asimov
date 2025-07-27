package com.gtcafe.asimov.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.gtcafe.asimov.framework.bean.request.HttpRequestContextBean;
import com.gtcafe.asimov.framework.utils.TimeUtils;
import com.gtcafe.asimov.infrastructure.queue.Producer;
import com.gtcafe.asimov.system.hello.HelloMapper;
import com.gtcafe.asimov.system.hello.config.HelloQueueConfig;
import com.gtcafe.asimov.system.hello.service.HelloCacheService;
import com.gtcafe.asimov.system.hello.service.HelloMetricsService;
import com.gtcafe.asimov.system.hello.service.HelloValidationService;

import io.micrometer.core.instrument.Timer;

import static org.mockito.Mockito.*;

@TestConfiguration
public class HelloIntegrationTestConfiguration {

    @Bean
    @Primary
    public Producer producer() {
        return mock(Producer.class);
    }

    @Bean
    @Primary
    public HelloQueueConfig helloQueueConfig() {
        HelloQueueConfig config = new HelloQueueConfig();
        config.setQueueName("test.hello.queue");
        config.setExchangeName("test.hello.exchange");
        config.setRoutingKeyName("test.hello.routing.key");
        return config;
    }

    @Bean
    @Primary
    public HelloCacheService helloCacheService() {
        return mock(HelloCacheService.class);
    }

    @Bean
    @Primary
    public HelloValidationService helloValidationService() {
        return mock(HelloValidationService.class);
    }

    @Bean
    @Primary
    public HelloMetricsService helloMetricsService() {
        HelloMetricsService mockService = mock(HelloMetricsService.class);
        Timer.Sample mockSample = mock(Timer.Sample.class);
        
        // Mock all timer methods to return the mock sample
        when(mockService.startSyncProcessingTimer()).thenReturn(mockSample);
        when(mockService.startAsyncProcessingTimer()).thenReturn(mockSample);
        when(mockService.startValidationTimer()).thenReturn(mockSample);
        when(mockService.startCacheOperationTimer()).thenReturn(mockSample);
        when(mockService.startDatabaseOperationTimer()).thenReturn(mockSample);
        
        return mockService;
    }

    @Bean
    @Primary
    public HelloMapper helloMapper() {
        return mock(HelloMapper.class);
    }

    @Bean
    @Primary
    public TimeUtils timeUtils() {
        return mock(TimeUtils.class);
    }

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        return mock(RedisConnectionFactory.class);
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        return mock(RedisTemplate.class);
    }

    @Bean
    @Primary
    public HttpRequestContextBean httpRequestContextBean() {
        return mock(HttpRequestContextBean.class);
    }
}