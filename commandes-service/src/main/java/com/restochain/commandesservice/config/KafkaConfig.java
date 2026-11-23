package com.restochain.commandesservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    public static final String TOPIC_COMMANDES_EVENTS = "commandes-events";

    @Bean
    public NewTopic commandesEventsTopic() {
        return TopicBuilder.name(TOPIC_COMMANDES_EVENTS)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
