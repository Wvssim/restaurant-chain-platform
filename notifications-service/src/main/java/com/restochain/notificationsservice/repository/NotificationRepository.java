package com.restochain.notificationsservice.repository;

import com.restochain.notificationsservice.entite.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByOrderByDateReceptionDesc();

    List<Notification> findByCommandeIdOrderByDateReceptionDesc(Long commandeId);
}
