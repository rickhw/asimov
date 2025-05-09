package com.gtcafe.asimov.infrastructure.queue.model;

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
