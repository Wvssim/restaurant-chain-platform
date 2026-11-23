package com.restochain.notificationsservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_COMMANDES = "commandes.events";
    public static final String QUEUE_NOTIFICATIONS = "notifications.queue";

    @Bean
    public TopicExchange commandesExchange() {
        return new TopicExchange(EXCHANGE_COMMANDES);
    }

    @Bean
    public Queue notificationsQueue() {
        return new Queue(QUEUE_NOTIFICATIONS, true);
    }

    @Bean
    public Binding binding(Queue notificationsQueue, TopicExchange commandesExchange) {
        return BindingBuilder.bind(notificationsQueue).to(commandesExchange).with("commande.#");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
