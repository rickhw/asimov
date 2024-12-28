package com.gtcafe.asimov.system.queue;

import lombok.Getter;
import lombok.Setter;

public class QueueConfig {
    @Getter @Setter
    private String name;

    @Getter @Setter
    private String exchange;

    @Getter @Setter
    private String routingKey;

}
