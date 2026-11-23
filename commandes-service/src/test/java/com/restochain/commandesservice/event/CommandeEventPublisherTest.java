package com.restochain.commandesservice.event;

import com.restochain.commandesservice.config.KafkaConfig;
import com.restochain.commandesservice.entite.Commande;
import com.restochain.commandesservice.enums.StatutCommande;
import org.apache.kafka.common.KafkaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommandeEventPublisherTest {

    @Mock
    private KafkaTemplate<String, CommandeEvent> kafkaTemplate;

    private CommandeEventPublisher publisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        publisher = new CommandeEventPublisher(kafkaTemplate);
    }

    private static Commande commande() {
        Commande commande = new Commande();
        commande.setId(1L);
        commande.setSuccursaleId(2L);
        commande.setNumeroTable(5);
        commande.setStatut(StatutCommande.RECUE);
        return commande;
    }

    @SuppressWarnings("unchecked")
    private void stubEnvoiReussi() {
        when(kafkaTemplate.send(any(String.class), any(String.class), any(CommandeEvent.class)))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));
    }

    @Test
    void publierCreation_envoie_sur_le_bon_topic_avec_la_cle_de_la_commande() {
        stubEnvoiReussi();

        publisher.publierCreation(commande());

        verify(kafkaTemplate).send(eq(KafkaConfig.TOPIC_COMMANDES_EVENTS), eq("1"), any(CommandeEvent.class));
    }

    @Test
    void publierChangementStatut_envoie_sur_le_meme_topic() {
        stubEnvoiReussi();

        publisher.publierChangementStatut(commande());

        verify(kafkaTemplate).send(eq(KafkaConfig.TOPIC_COMMANDES_EVENTS), eq("1"), any(CommandeEvent.class));
    }

    @Test
    void echec_synchrone_ne_remonte_pas_d_exception() {
        doThrow(new KafkaException("broker injoignable"))
                .when(kafkaTemplate).send(any(String.class), any(String.class), any(CommandeEvent.class));

        assertDoesNotThrow(() -> publisher.publierCreation(commande()));
    }

    @Test
    void echec_asynchrone_ne_remonte_pas_d_exception() {
        CompletableFuture<SendResult<String, CommandeEvent>> futureEnEchec = new CompletableFuture<>();
        futureEnEchec.completeExceptionally(new KafkaException("timeout d'envoi"));
        when(kafkaTemplate.send(any(String.class), any(String.class), any(CommandeEvent.class)))
                .thenReturn(futureEnEchec);

        assertDoesNotThrow(() -> publisher.publierCreation(commande()));
    }
}
