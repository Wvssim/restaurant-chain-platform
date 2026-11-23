package com.restochain.notificationsservice.event;

import java.util.Date;

/**
 * Vue minimale de l'evenement publie par commandes-service, telle que recue sur RabbitMQ.
 */
public class CommandeEvent {
    private Long commandeId;
    private Long succursaleId;
    private Integer numeroTable;
    private String statut;
    private String message;
    private Date dateEvenement;

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

    public Date getDateEvenement() {
        return dateEvenement;
    }

    public void setDateEvenement(Date dateEvenement) {
        this.dateEvenement = dateEvenement;
    }
}
