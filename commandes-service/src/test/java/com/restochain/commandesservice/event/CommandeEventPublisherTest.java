package com.restochain.commandesservice.event;

import com.restochain.commandesservice.config.RabbitMQConfig;
import com.restochain.commandesservice.entite.Commande;
import com.restochain.commandesservice.enums.StatutCommande;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class CommandeEventPublisherTest {

    @Mock
    private AmqpTemplate amqpTemplate;

    private CommandeEventPublisher publisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        publisher = new CommandeEventPublisher(amqpTemplate);
    }

    private static Commande commande() {
        Commande commande = new Commande();
        commande.setId(1L);
        commande.setSuccursaleId(2L);
        commande.setNumeroTable(5);
        commande.setStatut(StatutCommande.RECUE);
        return commande;
    }

    @Test
    void publierCreation_envoie_sur_le_bon_exchange_et_routing_key() {
        publisher.publierCreation(commande());

        verify(amqpTemplate).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE_COMMANDES),
                eq(CommandeEventPublisher.ROUTING_CREEE),
                any(CommandeEvent.class));
    }

    @Test
    void publierChangementStatut_envoie_sur_le_bon_routing_key() {
        publisher.publierChangementStatut(commande());

        verify(amqpTemplate).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE_COMMANDES),
                eq(CommandeEventPublisher.ROUTING_STATUT_CHANGEE),
                any(CommandeEvent.class));
    }

    @Test
    void rabbitmq_indisponible_ne_remonte_pas_d_exception() {
        doThrow(new AmqpException("broker injoignable"))
                .when(amqpTemplate).convertAndSend(any(String.class), any(String.class), any(Object.class));

        assertDoesNotThrow(() -> publisher.publierCreation(commande()));
    }
}
