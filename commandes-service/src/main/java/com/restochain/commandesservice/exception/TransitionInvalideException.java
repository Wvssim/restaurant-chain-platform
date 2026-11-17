package com.restochain.commandesservice.exception;

import com.restochain.commandesservice.enums.StatutCommande;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TransitionInvalideException extends RuntimeException {
    public TransitionInvalideException(StatutCommande statutActuel, StatutCommande statutDemande) {
        super("Transition de statut invalide : " + statutActuel + " -> " + statutDemande);
    }
}
