package com.example.monitoring_service.config;

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
}