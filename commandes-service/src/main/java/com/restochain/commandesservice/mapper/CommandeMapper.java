package com.restochain.commandesservice.mapper;

import com.restochain.commandesservice.dto.CommandeDto;
import com.restochain.commandesservice.dto.CreateCommandeDto;
import com.restochain.commandesservice.entite.Commande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommandeMapper {
    CommandeDto toDto(Commande commande);

    @Mapping(target = "lignesCommande", ignore = true)
    Commande toEntity(CreateCommandeDto createCommandeDto);
}
