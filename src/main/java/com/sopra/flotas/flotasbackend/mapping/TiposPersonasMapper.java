package com.sopra.flotas.flotasbackend.mapping;


import com.sopra.albia.persistence.entity.TipospersonasEntity;
import com.sopra.flotas.flotasbackend.contract.generated.models.TiposPersonas;
import org.mapstruct.Mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValueMappingStrategy.RETURN_NULL;
import static org.mapstruct.NullValuePropertyMappingStrategy.SET_TO_NULL;

@Mapper(componentModel = "spring",
        nullValueMappingStrategy = RETURN_NULL,
        nullValuePropertyMappingStrategy = SET_TO_NULL,
        nullValueCheckStrategy = ALWAYS)
public interface TiposPersonasMapper {


    TiposPersonas mapTiposPersonas(TipospersonasEntity tipoPersona);

    TipospersonasEntity mapTiposPersonas2(TiposPersonas tiposPersonas);
    TiposPersonas mapTiposPersonas3(TiposPersonas tipoPersona);

}
