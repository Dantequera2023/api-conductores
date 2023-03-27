package com.sopra.flotas.flotasbackend.mapping;


import com.sopra.albia.persistence.entity.LicenciasconductorEntity;
import com.sopra.flotas.flotasbackend.contract.generated.models.Licencias;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValueMappingStrategy.RETURN_NULL;
import static org.mapstruct.NullValuePropertyMappingStrategy.SET_TO_NULL;

@Mapper(componentModel = "spring",
        nullValueMappingStrategy = RETURN_NULL,
        nullValuePropertyMappingStrategy = SET_TO_NULL,
        nullValueCheckStrategy = ALWAYS)
public interface LicenciasMapper {

    @Mapping(target = "estado.idEstado", source = "estadosByIdEstado.idEstado")
    @Mapping(target = "estado.descEstado", source = "estadosByIdEstado.descEstado")
    @Mapping(target = "idConductor", source = "conductoresByIdConductor.idConductor")
    Licencias mapLicencias(LicenciasconductorEntity licencias);

    @Mapping(target = "dtFechaexpedicion", source = "dtFechaexpedicion", qualifiedByName = "parseoFechas")
    @Mapping(target = "dtFecharenovacion", source = "dtFecharenovacion", qualifiedByName = "parseoFechas")
    @Mapping(target = "dtFechaultimamodificacion", source = "dtFechaultimamodificacion", qualifiedByName = "parseoFechas")
    @Mapping(target = "estadosByIdEstado.idEstado", source = "estado.idEstado")
    @Mapping(target = "conductoresByIdConductor.idConductor", source = "idConductor")
    LicenciasconductorEntity mapLicencias2(Licencias licencias);
    Licencias mapLicencias3(Licencias licencias);





    @Named("parseoFechas")
    default java.sql.Date fechas (String fecha){
        if (fecha != null){
            return java.sql.Date.valueOf(java.time.LocalDateTime.parse(fecha).toLocalDate());
        }else{
            return null;
        }
    }

}












