package com.restochain.notificationsservice.mapper;

import com.restochain.notificationsservice.dto.NotificationDto;
import com.restochain.notificationsservice.entite.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationDto toDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setCommandeId(notification.getCommandeId());
        dto.setSuccursaleId(notification.getSuccursaleId());
        dto.setNumeroTable(notification.getNumeroTable());
        dto.setStatut(notification.getStatut());
        dto.setMessage(notification.getMessage());
        dto.setDateReception(notification.getDateReception());
        return dto;
    }
}
