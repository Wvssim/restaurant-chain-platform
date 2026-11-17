package com.restochain.menuservice.entite;

import com.restochain.menuservice.enums.CategoriePlat;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "plat")
public class Plat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String nom;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriePlat categorie;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal prix = BigDecimal.ZERO;

    @Column(nullable = false)
    private Long succursaleId;

    private boolean disponible = true;

    @Column(nullable = false)
    private int qteDisponible;

    public Plat() {
    }

    public Plat(Long id, String nom, String description, CategoriePlat categorie, BigDecimal prix,
                Long succursaleId, boolean disponible, int qteDisponible) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.categorie = categorie;
        this.prix = prix;
        this.succursaleId = succursaleId;
        this.disponible = disponible;
        this.qteDisponible = qteDisponible;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoriePlat getCategorie() {
        return categorie;
    }

    public void setCategorie(CategoriePlat categorie) {
        this.categorie = categorie;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public Long getSuccursaleId() {
        return succursaleId;
    }

    public void setSuccursaleId(Long succursaleId) {
        this.succursaleId = succursaleId;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public int getQteDisponible() {
        return qteDisponible;
    }

    public void setQteDisponible(int qteDisponible) {
        this.qteDisponible = qteDisponible;
    }
}
