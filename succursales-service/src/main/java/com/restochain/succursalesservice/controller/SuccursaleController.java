package com.restochain.succursalesservice.controller;

import com.restochain.succursalesservice.dto.SuccursaleDto;
import com.restochain.succursalesservice.entite.Succursale;
import com.restochain.succursalesservice.mapper.SuccursaleMapper;
import com.restochain.succursalesservice.service.SuccursaleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/succursales")
public class SuccursaleController {
    private final SuccursaleService succursaleService;
    private final SuccursaleMapper succursaleMapper;

    public SuccursaleController(SuccursaleService succursaleService, SuccursaleMapper succursaleMapper) {
        this.succursaleService = succursaleService;
        this.succursaleMapper = succursaleMapper;
    }

    @GetMapping
    public ResponseEntity<List<SuccursaleDto>> getAllSuccursales() {
        return ResponseEntity.ok(succursaleService.getAllSuccursales().stream().map(succursaleMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccursaleDto> getSuccursaleById(@PathVariable Long id) {
        return ResponseEntity.ok(succursaleMapper.toDto(succursaleService.getSuccursaleById(id)));
    }

    @PostMapping("/new")
    public ResponseEntity<SuccursaleDto> ajouterSuccursale(@Valid @RequestBody SuccursaleDto succursaleDto) {
        Succursale succursale = succursaleMapper.toEntity(succursaleDto);
        succursale.setId(null);
        succursale = succursaleService.ajouterSuccursale(succursale);
        return ResponseEntity.created(URI.create("/succursales/" + succursale.getId()))
                .body(succursaleMapper.toDto(succursale));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccursaleDto> modifierSuccursale(@PathVariable Long id,
                                                              @Valid @RequestBody SuccursaleDto succursaleDto) {
        Succursale succursale = succursaleService.modifierSuccursale(id, succursaleMapper.toEntity(succursaleDto));
        return ResponseEntity.ok(succursaleMapper.toDto(succursale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerSuccursale(@PathVariable Long id) {
        succursaleService.supprimerSuccursale(id);
        return ResponseEntity.noContent().build();
    }
}
