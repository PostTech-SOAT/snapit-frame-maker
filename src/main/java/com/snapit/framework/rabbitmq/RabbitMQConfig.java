package com.snapit.framework.rabbitmq;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange framesExtractionFinishedExchange() {
        return new TopicExchange("frames-extraction-finished-exchange");
    }

    @Bean
    public TopicExchange framesExtractionFailedExchange() {
        return new TopicExchange("frames-extraction-failed-exchange");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}