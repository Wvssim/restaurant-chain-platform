package com.restochain.commandesservice.client.dto;

/**
 * Vue minimale de succursales-service telle que recue par commandes-service via Feign.
 */
public class SuccursaleClientDto {
    private Long id;
    private String nom;
    private String ville;
    private String statut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
