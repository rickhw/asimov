package com.gtcafe.asimov.infrastructure.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.Data;
import lombok.Getter;

@Configuration
@ConfigurationProperties(prefix = "infra.cache.redis")
@Data
public class CacheConfig {

    @Value("${default-ttl:3600}") // 預設 1 小時
    private long defaultTtl;

    @Value("${perfix-name:asimov}")
    private String defaultPrefixName;

    @Getter
    private String defaultPrefixNameDelimiter = ":";


    @Getter
    private TimeUnit defaultTimeUnit = TimeUnit.SECONDS;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(RedisProperties properties) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(properties.getHost());
        config.setPort(properties.getPort());
        config.setPassword(properties.getPassword());
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // /** Solution 1: <String, Object> */

        // // 1. 使用 Jackson2JsonRedisSerializer 來序列化不同的 Object
        // Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // ObjectMapper objectMapper = new ObjectMapper();

        // // 2. 啟用多態處理（保存類型資訊），以支援不同子類型的反序列化
        // objectMapper.activateDefaultTyping(BasicPolymorphicTypeValidator.builder().build(),
        //         ObjectMapper.DefaultTyping.NON_FINAL);
        // objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        // serializer.setObjectMapper(objectMapper);

        // // 3. set Serializer
        // template.setKeySerializer(new StringRedisSerializer());
        // template.setValueSerializer(serializer);

        // // 4.
        // template.afterPropertiesSet();

        /** Solution 2: <String, String> */
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }
}
