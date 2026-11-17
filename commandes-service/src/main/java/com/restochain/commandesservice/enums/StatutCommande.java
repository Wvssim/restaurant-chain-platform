package com.restochain.commandesservice.enums;

/**
 * Cycle de vie d'une commande en salle :
 * RECUE -> EN_PREPARATION -> PRETE -> SERVIE -> PAYEE
 * avec possibilite d'annulation tant que la commande n'est pas servie.
 * Les transitions autorisees sont centralisees dans TransitionStatutService.
 */
public enum StatutCommande {
    RECUE,
    EN_PREPARATION,
    PRETE,
    SERVIE,
    PAYEE,
    ANNULEE
}
