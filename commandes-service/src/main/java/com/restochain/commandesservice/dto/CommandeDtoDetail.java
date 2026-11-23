package com.restochain.commandesservice.dto;

import com.restochain.commandesservice.enums.StatutCommande;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CommandeDtoDetail {
    private Long id;
    private Long succursaleId;
    private String succursaleNom;
    private String succursaleVille;
    private Integer numeroTable;
    private Date dateCommande;
    private List<LigneCommandeDto> lignesCommande;
    private BigDecimal prixTotal;
    private StatutCommande statut;

    public String getSuccursaleNom() {
        return succursaleNom;
    }

    public void setSuccursaleNom(String succursaleNom) {
        this.succursaleNom = succursaleNom;
    }

    public String getSuccursaleVille() {
        return succursaleVille;
    }

    public void setSuccursaleVille(String succursaleVille) {
        this.succursaleVille = succursaleVille;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public List<LigneCommandeDto> getLignesCommande() {
        return lignesCommande;
    }

    public void setLignesCommande(List<LigneCommandeDto> lignesCommande) {
        this.lignesCommande = lignesCommande;
    }

    public BigDecimal getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(BigDecimal prixTotal) {
        this.prixTotal = prixTotal;
    }

    public StatutCommande getStatut() {
        return statut;
    }

    public void setStatut(StatutCommande statut) {
        this.statut = statut;
    }
}
