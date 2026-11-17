package com.restochain.commandesservice.dto;

import com.restochain.commandesservice.enums.StatutCommande;
import jakarta.validation.constraints.NotNull;

public class ChangerStatutDto {
    @NotNull(message = "Le nouveau statut est obligatoire")
    private StatutCommande nouveauStatut;

    public StatutCommande getNouveauStatut() {
        return nouveauStatut;
    }

    public void setNouveauStatut(StatutCommande nouveauStatut) {
        this.nouveauStatut = nouveauStatut;
    }
}
