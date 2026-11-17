package com.restochain.commandesservice.service;

import com.restochain.commandesservice.exception.TransitionInvalideException;
import org.junit.jupiter.api.Test;

import static com.restochain.commandesservice.enums.StatutCommande.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransitionStatutValidatorTest {

    private final TransitionStatutValidator validator = new TransitionStatutValidator();

    @Test
    void parcours_nominal_autorise() {
        assertDoesNotThrow(() -> validator.verifierTransition(RECUE, EN_PREPARATION));
        assertDoesNotThrow(() -> validator.verifierTransition(EN_PREPARATION, PRETE));
        assertDoesNotThrow(() -> validator.verifierTransition(PRETE, SERVIE));
        assertDoesNotThrow(() -> validator.verifierTransition(SERVIE, PAYEE));
    }

    @Test
    void annulation_possible_avant_preparation_terminee() {
        assertDoesNotThrow(() -> validator.verifierTransition(RECUE, ANNULEE));
        assertDoesNotThrow(() -> validator.verifierTransition(EN_PREPARATION, ANNULEE));
    }

    @Test
    void on_ne_peut_pas_sauter_une_etape() {
        assertThrows(TransitionInvalideException.class, () -> validator.verifierTransition(RECUE, PRETE));
        assertThrows(TransitionInvalideException.class, () -> validator.verifierTransition(RECUE, SERVIE));
    }

    @Test
    void on_ne_peut_pas_annuler_une_commande_deja_servie() {
        assertThrows(TransitionInvalideException.class, () -> validator.verifierTransition(SERVIE, ANNULEE));
        assertThrows(TransitionInvalideException.class, () -> validator.verifierTransition(PRETE, ANNULEE));
    }

    @Test
    void les_etats_terminaux_n_ont_aucune_transition_sortante() {
        assertThrows(TransitionInvalideException.class, () -> validator.verifierTransition(PAYEE, RECUE));
        assertThrows(TransitionInvalideException.class, () -> validator.verifierTransition(ANNULEE, RECUE));
    }
}
