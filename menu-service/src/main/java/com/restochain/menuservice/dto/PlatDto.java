package com.restochain.menuservice.dto;

import com.restochain.menuservice.enums.CategoriePlat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class PlatDto {
    private Long id;

    @NotBlank(message = "Le nom du plat est obligatoire")
    private String nom;

    private String description;

    @NotNull(message = "La categorie est obligatoire")
    private CategoriePlat categorie;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit etre positif")
    private BigDecimal prix;

    @NotNull(message = "La succursale est obligatoire")
    private Long succursaleId;

    private boolean disponible = true;

    @Min(value = 0, message = "La quantite disponible ne peut pas etre negative")
    private int qteDisponible;

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
