package com.gtcafe.asimov.framework.bean.singleton;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "asimov.time")
public class TimeConfig {

    @Getter @Setter    
    private String zone;

    @Getter @Setter
    private String format;

}
