package com.restochain.succursalesservice.mapper;

import com.restochain.succursalesservice.dto.SuccursaleDto;
import com.restochain.succursalesservice.entite.Succursale;
import org.mapstruct.Mapper;

@Mapper
public interface SuccursaleMapper {
    SuccursaleDto toDto(Succursale succursale);

    Succursale toEntity(SuccursaleDto dto);
}
