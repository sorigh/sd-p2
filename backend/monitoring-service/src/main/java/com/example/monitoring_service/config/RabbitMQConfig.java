package com.example.monitoring_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${monitoring.queue.data}")
    private String deviceDataQueueName;

    @Value("${monitoring.queue.sync}")
    private String syncQueueName;
    

    public static final String SYNC_EXCHANGE = "sync_exchange"; 
    public static final String DEVICE_CREATED_KEY = "device.created";
    public static final String DEVICE_CREATED_QUEUE = "monitoring_device_created_queue";

    public static final String USER_CREATED_KEY = "user.created";
    public static final String USER_CREATED_QUEUE = "monitoring_user_created_queue";

    // Defines the queue for device data messages
    @Bean
    public Queue deviceDataQueue() {
        return new Queue(deviceDataQueueName, true); 
    }

    // Defines the queue for synchronization events
    @Bean
    public Queue syncQueue() {
        return new Queue(syncQueueName, true); 
    }
    
    // Configures Spring to automatically convert JSON to Java objects
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    // in all microservices
    @Bean
    public DirectExchange syncExchange() {
        return new DirectExchange(SYNC_EXCHANGE);
    }

    @Bean
    public Queue deviceCreatedQueue() {
        return new Queue(DEVICE_CREATED_QUEUE, true); 
    }

    // Binding pentru Device Created
    @Bean
    public Binding deviceCreatedBinding(Queue deviceCreatedQueue, DirectExchange syncExchange) {
        return BindingBuilder.bind(deviceCreatedQueue)
                .to(syncExchange)
                .with(DEVICE_CREATED_KEY);
    }


    // for user
    // Coada pentru User Created
    @Bean
    public Queue userCreatedQueue() {
        return new Queue(USER_CREATED_QUEUE, true); 
    }

    // Binding for User Created
    @Bean
    public Binding userCreatedBinding(Queue userCreatedQueue, DirectExchange syncExchange) {
        return BindingBuilder.bind(userCreatedQueue)
                .to(syncExchange)
                .with(USER_CREATED_KEY);
    }
}