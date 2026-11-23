package com.restochain.commandesservice.mapper;

import com.restochain.commandesservice.dto.CommandeDtoDetail;
import com.restochain.commandesservice.entite.Commande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * succursaleNom / succursaleVille ne viennent pas de l'entite : ils sont renseignes
 * apres coup par CommandeService.enrichir(), via un appel Feign a succursales-service.
 */
@Mapper(uses = LigneCommandeMapper.class)
public interface CommandeMapperDetail {
    @Mapping(target = "succursaleNom", ignore = true)
    @Mapping(target = "succursaleVille", ignore = true)
    CommandeDtoDetail toDto(Commande commande);
}
