package com.gtcafe.asimov.infrastructure.queue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.gtcafe.asimov.framework.constants.QueueName;
import com.gtcafe.asimov.infrastructure.queue.model.QueueConfig;

@Configuration
@ConfigurationProperties(prefix = "asimov.rabbitmq")
public class RabbitInitializer {

    @Value("${asimov.rabbitmq.autoInit:true}")
    private boolean autoInit;

    @Value("${asimov.rabbitmq.reset:false}")
    private boolean reset;

    // @Value("${asimov.rabbitmq.prefixName:asimov}")
    // private String prefixName;
    

    private List<QueueConfig> queues;
    private Map<String, QueueConfig> queueMap = new HashMap<>();

    public List<QueueConfig> getQueues() { return queues; }
    public void setQueues(List<QueueConfig> queues) { this.queues = queues; }

    public QueueConfig getQueueConfig(String queueName) {
		return queueMap.get(queueName);
	}

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    @DependsOn("rabbitAdmin")
    public CommandLineRunner initRabbitMQ(RabbitAdmin rabbitAdmin) {
        return args -> {
            System.out.println("Initiate the Queue ... ");
            if (reset) {
                System.out.println("Reset flag is true, deleting existing queues...");
                for (QueueConfig config : queues) {
                    rabbitAdmin.deleteQueue(config.getName());
                    System.out.printf("  - Queue [%s] deleted.\n", config.getName());
                }
            }

            if (autoInit) {
                System.out.println("AutoInit is true, initializing queues...");
                for (QueueConfig config : queues) {
                    Queue queue = new Queue(config.getName(), true);
                    rabbitAdmin.declareQueue(queue);

                    System.out.printf("  - Set Queue: [%s], exchange: [%s].\n", config.getName(), config.getExchange());

                    if (QueueName.FANOUT_EXCHANGE.equals(config.getExchange())) {
                        FanoutExchange exchange = new FanoutExchange(config.getExchange());
                        rabbitAdmin.declareExchange(exchange);
                        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange));
                    } else {
                        DirectExchange exchange = new DirectExchange(config.getExchange());
                        rabbitAdmin.declareExchange(exchange);
                        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(config.getRoutingKey()));
                    }

                    queueMap.put(config.getName(), config);
                }
                System.out.println("RabbitMQ Queues, Exchanges, and Bindings initialized.");
            }
        };
    }
}
