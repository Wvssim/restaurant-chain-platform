package com.restochain.commandesservice.client;

import com.restochain.commandesservice.client.dto.PlatClientDto;
import com.restochain.commandesservice.client.dto.ReservationStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Client Feign vers menu-service, resolu via Eureka.
 */
@FeignClient(name = "menu-service")
public interface MenuClient {

    @GetMapping("/plats/{id}")
    PlatClientDto getPlatById(@PathVariable("id") Long id);

    @PostMapping("/plats/{id}/stock/reserver")
    PlatClientDto reserverStock(@PathVariable("id") Long id, @RequestBody ReservationStockRequest reservation);
}
