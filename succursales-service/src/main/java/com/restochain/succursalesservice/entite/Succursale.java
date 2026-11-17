package com.restochain.succursalesservice.entite;

import com.restochain.succursalesservice.enums.StatutSuccursale;
import jakarta.persistence.*;

@Entity
@Table(name = "succursale")
public class Succursale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String nom;
    private String adresse;
    private String ville;
    private String telephone;
    private String responsable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutSuccursale statut = StatutSuccursale.OUVERTE;

    public Succursale() {
    }

    public Succursale(Long id, String nom, String adresse, String ville, String telephone, String responsable, StatutSuccursale statut) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.ville = ville;
        this.telephone = telephone;
        this.responsable = responsable;
        this.statut = statut;
    }

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
