package com.sopra.flotas.flotasbackend.mapping;


import com.sopra.albia.persistence.entity.CentrosEntity;
import com.sopra.albia.persistence.entity.ConductoresEntity;
import com.sopra.albia.persistence.entity.ConductorescentrosEntity;
import com.sopra.flotas.flotasbackend.contract.generated.models.Centro;
import com.sopra.flotas.flotasbackend.contract.generated.models.Conductor;
import com.sopra.flotas.flotasbackend.contract.generated.models.ConductoresDescripciones;
import com.sopra.flotas.flotasbackend.contract.generated.models.ConductoresDescripcionesPage;
import com.sopra.flotas.flotasbackend.mapping.util.ConductoresMapperUtil;
import com.sopra.flotas.flotasbackend.persistence.entity.projection.ConductorFilterResultProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValueMappingStrategy.RETURN_NULL;
import static org.mapstruct.NullValuePropertyMappingStrategy.SET_TO_NULL;

@Mapper(componentModel = "spring",
        nullValueMappingStrategy = RETURN_NULL,
        nullValuePropertyMappingStrategy = SET_TO_NULL,
        nullValueCheckStrategy = ALWAYS, uses = ConductoresMapperUtil.class)

public interface ConductoresMapper {


    @Mapping(target = "codCentro", source = "conductorescentrosByIdConductor", qualifiedByName = "obtenerCodCentroPrimero")
    @Mapping(target = "codTerritorial", source = "territorialesByCodTerritorial.codTerritorial")
    @Mapping(target = "idEstado", source = "estadosByIdEstado.idEstado")
    @Mapping(target = "idTipopersona", source = "tipospersonasByIdTipopersona.idTipopersona")
    Conductor mapConductoresEntityToDto(ConductoresEntity conductores);


    @Mapping(target = "centro", source = "conductorescentrosByIdConductor", qualifiedByName = "convertirCentroConductorToCentro")
    @Mapping(target = "territorial.codTerritorial", source = "territorialesByCodTerritorial.codTerritorial")
    @Mapping(target = "territorial.descTerritorial", source = "territorialesByCodTerritorial.descTerritorial")
    @Mapping(target = "tipoPersona.descTipopersona", source = "tipospersonasByIdTipopersona.descTipopersona")
    @Mapping(target = "estado.descEstado", source = "estadosByIdEstado.descEstado")
    ConductoresDescripciones mapConductoresToDtoDescripciones(ConductoresEntity conductores);

    @Mapping(target = "centro", source = "conductorescentrosByIdConductor", qualifiedByName = "convertirCentroConductorToCentro")
    @Mapping(target = "territorial.codTerritorial", source = "territorialesByCodTerritorial.codTerritorial")
    ConductoresDescripcionesPage mapConductoresToDtoDescripcionesPage(ConductoresEntity conductores);

    @Mapping(target = "dtFechaultimamodificacion", source = "dtFechaultimamodificacion", qualifiedByName = "parseoFechas")
    @Mapping(target = "conductorescentrosByIdConductor", source = ".", qualifiedByName = "convertirConductorDescripcionToConductoresCentrosEntity")
    @Mapping(target = "territorialesByCodTerritorial.codTerritorial", source = "territorial.codTerritorial")
    @Mapping(target = "tipospersonasByIdTipopersona", source = "tipoPersona")
    @Mapping(target = "estadosByIdEstado", source = "estado")
    ConductoresEntity mapConductoresDescripcionesToConductoresEntity(ConductoresDescripciones conductores);


    @Mapping(target = "centro", source = "conductorescentrosByIdConductor", qualifiedByName = "convertirCentroConductorToCentro")
    @Mapping(target = "territorial.codTerritorial", source = "territorialesByCodTerritorial.codTerritorial")
    @Mapping(target = "territorial.descTerritorial", source = "territorialesByCodTerritorial.descTerritorial")
    ConductoresDescripcionesPage mapConductoresProjectionToDtoDescripcionesPage(ConductorFilterResultProjection conductores);


    @Mapping(target = "dtFechaultimamodificacion", source = "dtFechaultimamodificacion", qualifiedByName = "parseoFechas")
    @Mapping(target = "tipospersonasByIdTipopersona.idTipopersona", source = "idTipopersona")
   ConductoresEntity mapConductoresToEntityFilter2(Conductor conductor);

    @Named("parseoFechas")
    default java.sql.Date fechas(String fecha) {
        if (fecha != null) {
            return java.sql.Date.valueOf(java.time.LocalDateTime.parse(fecha).toLocalDate());
        } else {
            return null;
        }
    }

    @Mapping(target = "baseTrabajo", source = "descCentro")
    @Mapping(target = "codCentro", source = "codCentro")
    @Mapping(target = "localidad", source = "localidadesByCodLocalidad.descLocalidad")
    @Mapping(target = "provincia", source = "localidadesByCodLocalidad.provinciasByCodProvincia.descProvincia")
    @Mapping(target = "comunidadAutonoma", source = "localidadesByCodLocalidad.provinciasByCodProvincia.ccaasByCodCcaa.descCcaa")
    Centro centroEntityToCentro(CentrosEntity centrosEntity);

    @Mapping(source = "baseTrabajo", target = "descCentro")
    @Mapping(source = "codCentro", target = "codCentro")
    @Mapping(source = "localidad", target = "localidadesByCodLocalidad.descLocalidad")
    @Mapping(source = "provincia", target = "localidadesByCodLocalidad.provinciasByCodProvincia.descProvincia")
    @Mapping(source = "comunidadAutonoma", target = "localidadesByCodLocalidad.provinciasByCodProvincia.ccaasByCodCcaa.descCcaa")
    CentrosEntity centroToCentroEntity(Centro centro);

    @Named("convertirCentroConductorToCentro")
    default Centro convertirCentroConductorToCentro(Collection<ConductorescentrosEntity> conductorescentrosEntities) {
        if (conductorescentrosEntities.isEmpty()) {
            return null;
        }
        return this.centroEntityToCentro(conductorescentrosEntities.stream().findFirst().get().getCentrosByCodCentro());
    }

    @Named("obtenerCodCentroPrimero")
    default List<String> obtenerCodCentroPrimero(Collection<ConductorescentrosEntity> conductorescentrosEntities) {
        if (conductorescentrosEntities.isEmpty()) {
            return null;
        }
        List<String> codCentros = new ArrayList<>();
        conductorescentrosEntities.stream().forEach(t -> codCentros.add(t.getCentrosByCodCentro().getCodCentro()));
        return codCentros;
    }

    @Named("convertirConductorDescripcionToConductoresCentrosEntity")
    default Collection<ConductorescentrosEntity> convertirConductorDescripcionToConductoresCentrosEntity(ConductoresDescripciones conductor) {
        if (conductor.getCentro() != null) {
            ConductorescentrosEntity conductoresEntity = new ConductorescentrosEntity();
            conductoresEntity.setConductoresByIdConductor(new ConductoresEntity());
            conductoresEntity.getConductoresByIdConductor().setIdConductor((conductor.getIdConductor().longValueExact()));
            conductoresEntity.setCentrosByCodCentro(this.centroToCentroEntity(conductor.getCentro()));
            return Arrays.asList(conductoresEntity);
        }
        return null;
    }

}
