package com.restochain.menuservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReservationStockDto {
    @NotNull(message = "La quantite est obligatoire")
    @Min(value = 1, message = "La quantite doit etre positive")
    private Integer quantite;

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }
}
