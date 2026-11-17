package com.restochain.commandesservice.mapper;

import com.restochain.commandesservice.dto.LigneCommandeDto;
import com.restochain.commandesservice.entite.LigneCommande;
import org.mapstruct.Mapper;

@Mapper
public interface LigneCommandeMapper {
    LigneCommandeDto toDto(LigneCommande ligneCommande);
}
