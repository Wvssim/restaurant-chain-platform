package com.restochain.commandesservice.mapper;

import com.restochain.commandesservice.dto.CommandeDtoDetail;
import com.restochain.commandesservice.entite.Commande;
import org.mapstruct.Mapper;

@Mapper(uses = LigneCommandeMapper.class)
public interface CommandeMapperDetail {
    CommandeDtoDetail toDto(Commande commande);
}
