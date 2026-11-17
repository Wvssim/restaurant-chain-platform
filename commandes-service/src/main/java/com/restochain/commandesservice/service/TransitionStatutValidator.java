package com.restochain.commandesservice.service;

import com.restochain.commandesservice.enums.StatutCommande;
import com.restochain.commandesservice.exception.TransitionInvalideException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static com.restochain.commandesservice.enums.StatutCommande.*;

/**
 * Centralise les transitions autorisees du workflow de commande :
 * <pre>
 *   RECUE ---------> EN_PREPARATION ------> PRETE ------> SERVIE ------> PAYEE
 *     |                    |
 *     +--------------------+----------------------------------------> ANNULEE
 * </pre>
 * PAYEE et ANNULEE sont des etats terminaux : aucune transition n'en repart.
 * Une commande servie ne peut plus etre annulee (le plat a deja ete donne au client).
 */
@Component
public class TransitionStatutValidator {

    private static final Map<StatutCommande, Set<StatutCommande>> TRANSITIONS_AUTORISEES = new EnumMap<>(StatutCommande.class);

    static {
        TRANSITIONS_AUTORISEES.put(RECUE, EnumSet.of(EN_PREPARATION, ANNULEE));
        TRANSITIONS_AUTORISEES.put(EN_PREPARATION, EnumSet.of(PRETE, ANNULEE));
        TRANSITIONS_AUTORISEES.put(PRETE, EnumSet.of(SERVIE));
        TRANSITIONS_AUTORISEES.put(SERVIE, EnumSet.of(PAYEE));
        TRANSITIONS_AUTORISEES.put(PAYEE, EnumSet.noneOf(StatutCommande.class));
        TRANSITIONS_AUTORISEES.put(ANNULEE, EnumSet.noneOf(StatutCommande.class));
    }

    public void verifierTransition(StatutCommande statutActuel, StatutCommande statutDemande) {
        Set<StatutCommande> transitionsPossibles = TRANSITIONS_AUTORISEES.get(statutActuel);
        if (transitionsPossibles == null || !transitionsPossibles.contains(statutDemande)) {
            throw new TransitionInvalideException(statutActuel, statutDemande);
        }
    }
}
