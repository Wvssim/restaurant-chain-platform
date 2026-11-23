package com.restochain.notificationsservice.listener;

import com.restochain.notificationsservice.config.RabbitMQConfig;
import com.restochain.notificationsservice.event.CommandeEvent;
import com.restochain.notificationsservice.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CommandeEventListener {
    private final NotificationService notificationService;

    public CommandeEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIFICATIONS)
    public void recevoir(CommandeEvent event) {
        notificationService.enregistrer(event);
    }
}
