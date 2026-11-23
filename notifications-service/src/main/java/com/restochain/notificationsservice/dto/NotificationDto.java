package com.restochain.notificationsservice.dto;

import java.util.Date;

public class NotificationDto {
    private Long id;
    private Long commandeId;
    private Long succursaleId;
    private Integer numeroTable;
    private String statut;
    private String message;
    private Date dateReception;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(Long commandeId) {
        this.commandeId = commandeId;
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateReception() {
        return dateReception;
    }

    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }
}
