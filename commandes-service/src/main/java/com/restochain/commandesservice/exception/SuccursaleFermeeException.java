package com.restochain.commandesservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SuccursaleFermeeException extends RuntimeException {
    public SuccursaleFermeeException() {
        super("Cette succursale est fermee, impossible d'y passer commande");
    }
}
