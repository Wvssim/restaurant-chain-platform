package com.restochain.commandesservice.client.dto;

public class ReservationStockRequest {
    private int quantite;

    public ReservationStockRequest() {
    }

    public ReservationStockRequest(int quantite) {
        this.quantite = quantite;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}
