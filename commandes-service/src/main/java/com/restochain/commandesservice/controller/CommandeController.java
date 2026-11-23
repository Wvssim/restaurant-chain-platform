package com.restochain.commandesservice.controller;

import com.restochain.commandesservice.dto.ChangerStatutDto;
import com.restochain.commandesservice.dto.CommandeDto;
import com.restochain.commandesservice.dto.CommandeDtoDetail;
import com.restochain.commandesservice.dto.CreateCommandeDto;
import com.restochain.commandesservice.entite.Commande;
import com.restochain.commandesservice.mapper.CommandeMapper;
import com.restochain.commandesservice.mapper.CommandeMapperDetail;
import com.restochain.commandesservice.service.CommandeService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/commandes")
public class CommandeController {
    private final CommandeService commandeService;
    private final CommandeMapper commandeMapper;
    private final CommandeMapperDetail commandeMapperDetail;

    public CommandeController(CommandeService commandeService, CommandeMapper commandeMapper, CommandeMapperDetail commandeMapperDetail) {
        this.commandeService = commandeService;
        this.commandeMapper = commandeMapper;
        this.commandeMapperDetail = commandeMapperDetail;
    }

    @PostMapping("/new")
    public ResponseEntity<CommandeDtoDetail> createCommande(@Valid @RequestBody CreateCommandeDto createCommandeDto,
                                                              @NotBlank @Size(max = 255) @RequestHeader(name = "idempotencyKey") String idempotency) {
        Commande commande = commandeService.createCommande(createCommandeDto, idempotency);
        CommandeDtoDetail dto = commandeMapperDetail.toDto(commande);
        commandeService.enrichir(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<CommandeDto>> getListCommandes() {
        return ResponseEntity.ok(commandeService.listallCommande().stream().map(commandeMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeDtoDetail> getCommandeById(@PathVariable Long id) {
        Commande commande = commandeService.getCommandeById(id);
        CommandeDtoDetail dto = commandeMapperDetail.toDto(commande);
        commandeService.enrichir(dto);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<CommandeDtoDetail> changerStatut(@PathVariable Long id,
                                                             @Valid @RequestBody ChangerStatutDto changerStatutDto) {
        Commande commande = commandeService.changerStatut(id, changerStatutDto.getNouveauStatut());
        CommandeDtoDetail dto = commandeMapperDetail.toDto(commande);
        commandeService.enrichir(dto);
        return ResponseEntity.ok(dto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> donneesInvalides(ConstraintViolationException exception) {
        return ResponseEntity.badRequest().body("Donnees invalides");
    }
}
