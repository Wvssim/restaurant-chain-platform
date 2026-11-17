package com.restochain.succursalesservice.dto;

import com.restochain.succursalesservice.enums.StatutSuccursale;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SuccursaleDto {
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    @NotBlank(message = "La ville est obligatoire")
    private String ville;

    @NotBlank(message = "Le telephone est obligatoire")
    private String telephone;

    private String responsable;

    @NotNull(message = "Le statut est obligatoire")
    private StatutSuccursale statut;

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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public StatutSuccursale getStatut() {
        return statut;
    }

    public void setStatut(StatutSuccursale statut) {
        this.statut = statut;
    }
}
