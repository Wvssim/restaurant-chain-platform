package com.restochain.menuservice.mapper;

import com.restochain.menuservice.dto.PlatDto;
import com.restochain.menuservice.entite.Plat;
import org.mapstruct.Mapper;

@Mapper
public interface PlatMapper {
    PlatDto toDto(Plat plat);

    Plat toEntity(PlatDto dto);
}
