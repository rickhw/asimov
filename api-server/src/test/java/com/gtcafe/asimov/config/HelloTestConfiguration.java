package com.gtcafe.asimov.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.gtcafe.asimov.framework.bean.request.HttpRequestContextBean;
import com.gtcafe.asimov.framework.bean.request.RequestId;

import static org.mockito.Mockito.*;

@TestConfiguration
public class HelloTestConfiguration {

    @Bean
    @Primary
    public HttpRequestContextBean httpRequestContextBean() {
        HttpRequestContextBean mockBean = mock(HttpRequestContextBean.class);
        RequestId mockRequestId = mock(RequestId.class);
        when(mockRequestId.getRequestId()).thenReturn("test-request-id");
        when(mockBean.getRequestId()).thenReturn(mockRequestId);
        return mockBean;
    }

    @Bean
    @Primary
    public org.springframework.amqp.core.AmqpTemplate amqpTemplate() {
        return mock(org.springframework.amqp.core.AmqpTemplate.class);
    }

    @Bean
    @Primary
    public com.gtcafe.asimov.infrastructure.queue.RabbitInitializer rabbitInitializer() {
        return mock(com.gtcafe.asimov.infrastructure.queue.RabbitInitializer.class);
    }

    @Bean
    @Primary
    public com.gtcafe.asimov.framework.utils.JsonUtils jsonUtils() {
        return mock(com.gtcafe.asimov.framework.utils.JsonUtils.class);
    }
}