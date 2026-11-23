package com.restochain.commandesservice.dto;

import java.math.BigDecimal;

public class LigneCommandeDto {
    private Long id;
    private Long platId;
    private String platNom;
    private int quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal sousTotal;

    public String getPlatNom() {
        return platNom;
    }

    public void setPlatNom(String platNom) {
        this.platNom = platNom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlatId() {
        return platId;
    }

    public void setPlatId(Long platId) {
        this.platId = platId;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public BigDecimal getSousTotal() {
        return sousTotal;
    }

    public void setSousTotal(BigDecimal sousTotal) {
        this.sousTotal = sousTotal;
    }
}
