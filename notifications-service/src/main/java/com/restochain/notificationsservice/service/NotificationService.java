package com.restochain.notificationsservice.service;

import com.restochain.notificationsservice.entite.Notification;
import com.restochain.notificationsservice.event.CommandeEvent;
import com.restochain.notificationsservice.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification enregistrer(CommandeEvent event) {
        Notification notification = new Notification();
        notification.setCommandeId(event.getCommandeId());
        notification.setSuccursaleId(event.getSuccursaleId());
        notification.setNumeroTable(event.getNumeroTable());
        notification.setStatut(event.getStatut());
        notification.setMessage(event.getMessage());
        notification.setDateReception(new Date());

        Notification enregistree = notificationRepository.save(notification);
        log.info("Notification enregistree pour la commande {} : {}", event.getCommandeId(), event.getMessage());
        return enregistree;
    }

    public List<Notification> listerToutes() {
        return notificationRepository.findAllByOrderByDateReceptionDesc();
    }

    public List<Notification> listerParCommande(Long commandeId) {
        return notificationRepository.findByCommandeIdOrderByDateReceptionDesc(commandeId);
    }
}
