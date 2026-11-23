package com.restochain.commandesservice.mapper;

import com.restochain.commandesservice.dto.CommandeDto;
import com.restochain.commandesservice.entite.Commande;
import org.mapstruct.Mapper;

@Mapper
public interface CommandeMapper {
    CommandeDto toDto(Commande commande);
}
