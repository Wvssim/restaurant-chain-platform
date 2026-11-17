package com.restochain.menuservice.controller;

import com.restochain.menuservice.dto.PlatDto;
import com.restochain.menuservice.dto.ReservationStockDto;
import com.restochain.menuservice.entite.Plat;
import com.restochain.menuservice.mapper.PlatMapper;
import com.restochain.menuservice.service.PlatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/plats")
public class PlatController {
    private final PlatService platService;
    private final PlatMapper platMapper;

    public PlatController(PlatService platService, PlatMapper platMapper) {
        this.platService = platService;
        this.platMapper = platMapper;
    }

    @GetMapping
    public ResponseEntity<List<PlatDto>> getAllPlats(@RequestParam(required = false) Long succursaleId) {
        List<Plat> plats = succursaleId != null
                ? platService.getPlatsBySuccursale(succursaleId)
                : platService.getAllPlats();
        return ResponseEntity.ok(plats.stream().map(platMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatDto> getPlatById(@PathVariable Long id) {
        return ResponseEntity.ok(platMapper.toDto(platService.getPlatById(id)));
    }

    @PostMapping("/new")
    public ResponseEntity<PlatDto> ajouterPlat(@Valid @RequestBody PlatDto platDto) {
        Plat plat = platMapper.toEntity(platDto);
        plat.setId(null);
        plat = platService.ajouterPlat(plat);
        return ResponseEntity.created(URI.create("/plats/" + plat.getId()))
                .body(platMapper.toDto(plat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatDto> modifierPlat(@PathVariable Long id, @Valid @RequestBody PlatDto platDto) {
        Plat plat = platService.modifierPlat(id, platMapper.toEntity(platDto));
        return ResponseEntity.ok(platMapper.toDto(plat));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerPlat(@PathVariable Long id) {
        platService.supprimerPlat(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/stock/reserver")
    public ResponseEntity<PlatDto> reserverStock(@PathVariable Long id,
                                                   @Valid @RequestBody ReservationStockDto reservation) {
        return ResponseEntity.ok(platMapper.toDto(platService.reserverStock(id, reservation.getQuantite())));
    }
}
