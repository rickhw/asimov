package com.gtcafe.asimov.infrastructure.queue;

import org.jboss.logging.MDC;

public final class QueueMdcUtils {
    
    public static void Enqueue(String queueName, String exchangeName, String rountingKeyName) {
        MDC.put("QueueVendor", "RabbitMQ");
        MDC.put("QueueName", queueName);
        // MDC.put("CacheValue", value);
        MDC.put("ExchangeName", exchangeName);
        MDC.put("RoutingKey", rountingKeyName);

        MDC.put("QueueAction", "enqueue");
    }

    public static void Dequeue(String queueName, String exchangeName, String rountingKeyName) {
        MDC.put("QueueVendor", "RabbitMQ");
        MDC.put("QueueName", queueName);
        // MDC.put("CacheValue", value);
        MDC.put("ExchangeName", exchangeName);
        MDC.put("RoutingKey", rountingKeyName);

        MDC.put("QueueAction", "dequeue");
    }

    public static void Requeue(String queueName, String exchangeName, String rountingKeyName) {
        MDC.put("QueueVendor", "RabbitMQ");
        MDC.put("QueueName", queueName);
        // MDC.put("CacheValue", value);
        MDC.put("ExchangeName", exchangeName);
        MDC.put("RoutingKey", rountingKeyName);

        MDC.put("QueueAction", "requeue");
    }


}
