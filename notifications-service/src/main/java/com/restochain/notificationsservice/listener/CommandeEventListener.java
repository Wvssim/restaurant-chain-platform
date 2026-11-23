package com.restochain.notificationsservice.listener;

import com.restochain.notificationsservice.event.CommandeEvent;
import com.restochain.notificationsservice.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CommandeEventListener {
    public static final String TOPIC_COMMANDES_EVENTS = "commandes-events";

    private final NotificationService notificationService;

    public CommandeEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = TOPIC_COMMANDES_EVENTS, groupId = "notifications-service")
    public void recevoir(CommandeEvent event) {
        notificationService.enregistrer(event);
    }
}
