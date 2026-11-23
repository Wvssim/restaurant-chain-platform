package com.restochain.commandesservice.event;

import com.restochain.commandesservice.config.RabbitMQConfig;
import com.restochain.commandesservice.entite.Commande;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Publie les evenements du cycle de vie d'une commande sur RabbitMQ. La publication est
 * best-effort : si le broker est indisponible, l'echec est journalise sans remonter au
 * client, car il s'agit d'une notification annexe et non d'une donnee metier critique.
 */
@Component
public class CommandeEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(CommandeEventPublisher.class);

    public static final String ROUTING_CREEE = "commande.creee";
    public static final String ROUTING_STATUT_CHANGEE = "commande.statut.changee";

    private final AmqpTemplate amqpTemplate;

    public CommandeEventPublisher(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void publierCreation(Commande commande) {
        publier(ROUTING_CREEE, construireEvenement(commande, "Nouvelle commande recue"));
    }

    public void publierChangementStatut(Commande commande) {
        publier(ROUTING_STATUT_CHANGEE, construireEvenement(commande, "Statut de la commande mis a jour"));
    }

    private void publier(String routingKey, CommandeEvent event) {
        try {
            amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_COMMANDES, routingKey, event);
        } catch (AmqpException e) {
            log.warn("Publication de l'evenement '{}' impossible (RabbitMQ indisponible) : {}", routingKey, e.getMessage());
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
