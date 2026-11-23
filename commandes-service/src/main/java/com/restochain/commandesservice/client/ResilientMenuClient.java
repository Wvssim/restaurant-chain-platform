package com.restochain.commandesservice.client;

import com.restochain.commandesservice.client.dto.PlatClientDto;
import com.restochain.commandesservice.client.dto.ReservationStockRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;

/**
 * Enveloppe MenuClient avec Circuit Breaker + Retry (voir ResilientSuccursaleClient).
 */
@Component
public class ResilientMenuClient {
    private final MenuClient menuClient;

    public ResilientMenuClient(MenuClient menuClient) {
        this.menuClient = menuClient;
    }

    @Retry(name = "menuService")
    @CircuitBreaker(name = "menuService")
    public PlatClientDto getPlatById(Long id) {
        return menuClient.getPlatById(id);
    }

    @Retry(name = "menuService")
    @CircuitBreaker(name = "menuService")
    public PlatClientDto reserverStock(Long id, ReservationStockRequest reservation) {
        return menuClient.reserverStock(id, reservation);
    }
}
