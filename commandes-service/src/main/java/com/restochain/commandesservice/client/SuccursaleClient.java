package com.restochain.commandesservice.client;

import com.restochain.commandesservice.client.dto.SuccursaleClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Client Feign vers succursales-service, resolu via Eureka (nom = spring.application.name de la cible).
 */
@FeignClient(name = "succursales-service")
public interface SuccursaleClient {

    @GetMapping("/succursales/{id}")
    SuccursaleClientDto getSuccursaleById(@PathVariable("id") Long id);
}
