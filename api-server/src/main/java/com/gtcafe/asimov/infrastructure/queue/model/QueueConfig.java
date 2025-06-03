package com.gtcafe.asimov.infrastructure.queue.model;

import lombok.Data;

@Data
public class QueueConfig {
    private String name;
    private String exchange;
    private String routingKey;
}
