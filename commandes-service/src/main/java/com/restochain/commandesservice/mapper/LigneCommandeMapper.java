package com.restochain.commandesservice.mapper;

import com.restochain.commandesservice.dto.LigneCommandeDto;
import com.restochain.commandesservice.entite.LigneCommande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * platNom ne vient pas de l'entite : renseigne apres coup par CommandeService.enrichir(),
 * via un appel Feign a menu-service.
 */
@Mapper
public interface LigneCommandeMapper {
    @Mapping(target = "platNom", ignore = true)
    LigneCommandeDto toDto(LigneCommande ligneCommande);
}
