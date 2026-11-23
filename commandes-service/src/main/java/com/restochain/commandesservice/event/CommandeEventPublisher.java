package com.restochain.commandesservice.event;

import com.restochain.commandesservice.config.KafkaConfig;
import com.restochain.commandesservice.entite.Commande;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Publie les evenements du cycle de vie d'une commande sur Kafka (topic commandes-events,
 * cle = id de la commande pour garder l'ordre des evenements d'une meme commande sur une
 * meme partition). La publication est best-effort : un echec, synchrone (serialisation) ou
 * asynchrone (broker injoignable, via le callback du Future retourne par send()), est
 * journalise sans jamais remonter au client - il s'agit d'une notification annexe, pas
 * d'une donnee metier critique.
 */
@Component
public class CommandeEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(CommandeEventPublisher.class);

    private final KafkaTemplate<String, CommandeEvent> kafkaTemplate;

    public CommandeEventPublisher(KafkaTemplate<String, CommandeEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publierCreation(Commande commande) {
        publier(construireEvenement(commande, "Nouvelle commande recue"));
    }

    public void publierChangementStatut(Commande commande) {
        publier(construireEvenement(commande, "Statut de la commande mis a jour"));
    }

    private void publier(CommandeEvent event) {
        try {
            kafkaTemplate.send(KafkaConfig.TOPIC_COMMANDES_EVENTS, event.getCommandeId().toString(), event)
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            log.warn("Publication Kafka de l'evenement pour la commande {} impossible : {}",
                                    event.getCommandeId(), exception.getMessage());
                        }
                    });
        } catch (KafkaException e) {
            log.warn("Publication Kafka de l'evenement pour la commande {} impossible : {}", event.getCommandeId(), e.getMessage());
        }
    }

    private CommandeEvent construireEvenement(Commande commande, String message) {
        CommandeEvent event = new CommandeEvent();
        event.setCommandeId(commande.getId());
        event.setSuccursaleId(commande.getSuccursaleId());
        event.setNumeroTable(commande.getNumeroTable());
        event.setStatut(commande.getStatut().name());
        event.setMessage(message);
        event.setDateEvenement(new Date());
        return event;
    }
}
