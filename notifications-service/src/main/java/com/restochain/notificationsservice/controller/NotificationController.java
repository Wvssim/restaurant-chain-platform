package com.restochain.notificationsservice.controller;

import com.restochain.notificationsservice.dto.NotificationDto;
import com.restochain.notificationsservice.mapper.NotificationMapper;
import com.restochain.notificationsservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    public NotificationController(NotificationService notificationService, NotificationMapper notificationMapper) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getListNotifications() {
        return ResponseEntity.ok(notificationService.listerToutes().stream().map(notificationMapper::toDto).toList());
    }

    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<List<NotificationDto>> getNotificationsParCommande(@PathVariable Long commandeId) {
        return ResponseEntity.ok(notificationService.listerParCommande(commandeId).stream().map(notificationMapper::toDto).toList());
    }
}
