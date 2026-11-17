package com.restochain.commandesservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
public class CreationCommandeNonDisponibleException extends RuntimeException {
    public CreationCommandeNonDisponibleException() {
        super("Creation avec validation interservices (succursale + plats) : a realiser au jalon Feign.");
    }
}
