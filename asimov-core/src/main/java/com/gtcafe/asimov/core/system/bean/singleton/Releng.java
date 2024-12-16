package com.gtcafe.asimov.core.system.bean.singleton;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "app")
@PropertySource("classpath:releng.properties")
public class Releng {

    @Getter @Setter    
    private String productName;

    @Getter @Setter
    private String serviceName;

    @Getter @Setter
    private String version;

    @Getter @Setter
    private String buildType;

    @Getter @Setter
    private String buildId;

    @Getter @Setter
    private String hashcode;

}
