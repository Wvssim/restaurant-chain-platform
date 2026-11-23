package com.restochain.commandesservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StockInsuffisantException extends RuntimeException {
    public StockInsuffisantException(String nomPlat) {
        super("Quantite disponible insuffisante pour le plat : " + nomPlat);
    }
}
