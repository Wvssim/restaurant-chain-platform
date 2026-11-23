package com.restochain.notificationsservice.service;

import com.restochain.notificationsservice.entite.Notification;
import com.restochain.notificationsservice.event.CommandeEvent;
import com.restochain.notificationsservice.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationService = new NotificationService(notificationRepository);
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void enregistrer_copie_les_champs_de_l_evenement() {
        CommandeEvent event = new CommandeEvent();
        event.setCommandeId(1L);
        event.setSuccursaleId(2L);
        event.setNumeroTable(5);
        event.setStatut("RECUE");
        event.setMessage("Nouvelle commande recue");

        Notification notification = notificationService.enregistrer(event);

        assertEquals(1L, notification.getCommandeId());
        assertEquals(2L, notification.getSuccursaleId());
        assertEquals(5, notification.getNumeroTable());
        assertEquals("RECUE", notification.getStatut());
        assertEquals("Nouvelle commande recue", notification.getMessage());
        verify(notificationRepository).save(any(Notification.class));
    }
}
