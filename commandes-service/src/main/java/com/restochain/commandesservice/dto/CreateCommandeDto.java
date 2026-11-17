package com.restochain.commandesservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateCommandeDto {
    @NotNull(message = "La succursale est obligatoire")
    private Long succursaleId;

    @NotNull(message = "Le numero de table est obligatoire")
    private Integer numeroTable;

    @NotEmpty(message = "Une commande doit contenir au moins une ligne")
    @Valid
    private List<LigneCommandeCreationDto> lignes;

    public Long getSuccursaleId() {
        return succursaleId;
    }

    public void setSuccursaleId(Long succursaleId) {
        this.succursaleId = succursaleId;
    }

    public Integer getNumeroTable() {
        return numeroTable;
    }

    public void setNumeroTable(Integer numeroTable) {
        this.numeroTable = numeroTable;
    }

    public List<LigneCommandeCreationDto> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneCommandeCreationDto> lignes) {
        this.lignes = lignes;
    }

    public static class LigneCommandeCreationDto {
        @NotNull(message = "Le plat est obligatoire")
        private Long platId;

        @NotNull(message = "La quantite est obligatoire")
        private Integer quantite;

        public Long getPlatId() {
            return platId;
        }

        public void setPlatId(Long platId) {
            this.platId = platId;
        }

        public Integer getQuantite() {
            return quantite;
        }

        public void setQuantite(Integer quantite) {
            this.quantite = quantite;
        }
    }
}
