package com.restochain.menuservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StockInsuffisantException extends RuntimeException {
    public StockInsuffisantException() {
        super("Quantite disponible insuffisante pour ce plat");
    }
}
