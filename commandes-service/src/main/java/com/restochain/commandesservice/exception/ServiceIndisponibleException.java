package com.restochain.commandesservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Leve lorsqu'un appel Feign vers un service distant echoue pour une raison de communication
 * (service injoignable, timeout) plutot qu'une reponse metier (404/409 traites a part).
 * Point d'accroche pour le Circuit Breaker / Retry du jalon resilience.
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceIndisponibleException extends RuntimeException {
    public ServiceIndisponibleException(String nomService, Throwable cause) {
        super("Le service '" + nomService + "' est actuellement injoignable", cause);
    }
}
