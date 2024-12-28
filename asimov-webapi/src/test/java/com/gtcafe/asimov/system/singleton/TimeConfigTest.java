package com.gtcafe.asimov.system.singleton;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import com.gtcafe.asimov.system.bean.singleton.TimeConfig;

@SpringBootTest
@EnableConfigurationProperties(TimeConfig.class)
@TestPropertySource(properties = {
    "app.time.zone=UTC",
    "app.time.format=yyyy-MM-dd HH:mm:ss"
})
class TimeConfigTest {

    @Configuration
    @EnableConfigurationProperties(TimeConfig.class)
    static class TestConfig {
    }

    @Autowired
    private TimeConfig timeConfig;

    @Test
    void testTimeConfigPropertiesBinding() {
        assertThat(timeConfig).isNotNull();
        assertThat(timeConfig.getZone()).isEqualTo("UTC");
        assertThat(timeConfig.getFormat()).isEqualTo("yyyy-MM-dd HH:mm:ss");
    }

    @Test
    void testGetterAndSetter() {
        TimeConfig config = new TimeConfig();
        config.setZone("Asia/Taipei");
        config.setFormat("yyyy/MM/dd");

        assertThat(config.getZone()).isEqualTo("Asia/Taipei");
        assertThat(config.getFormat()).isEqualTo("yyyy/MM/dd");
    }
}
