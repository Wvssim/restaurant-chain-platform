package com.restochain.commandesservice.client;

import com.restochain.commandesservice.client.dto.SuccursaleClientDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;

/**
 * Enveloppe SuccursaleClient avec Circuit Breaker + Retry. Seules les pannes de connectivite
 * (feign.RetryableException, ex. succursales-service injoignable ou en timeout) sont retentees
 * et comptabilisees par le circuit breaker : une reponse metier (404, etc.) est deterministe et
 * ne doit ni etre retentee, ni compter comme une panne du service.
 */
@Component
public class ResilientSuccursaleClient {
    private final SuccursaleClient succursaleClient;

    public ResilientSuccursaleClient(SuccursaleClient succursaleClient) {
        this.succursaleClient = succursaleClient;
    }

    @Retry(name = "succursalesService")
    @CircuitBreaker(name = "succursalesService")
    public SuccursaleClientDto getSuccursaleById(Long id) {
        return succursaleClient.getSuccursaleById(id);
    }
}
