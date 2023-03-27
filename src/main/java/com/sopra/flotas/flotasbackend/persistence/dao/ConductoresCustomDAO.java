package com.sopra.flotas.flotasbackend.persistence.dao;

import com.sopra.albia.persistence.entity.ConductoresEntity;
import com.sopra.flotas.flotasbackend.persistence.entity.projection.ConductorFilterResultProjection;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface ConductoresCustomDAO {

    Page<ConductorFilterResultProjection> findConductoresByCentroTerriPersoLiceIdConductor(ConductoresEntity conductor, List<String> listCodCentro, List<String> listCodTerritorial,
                                                                                           List<BigDecimal> listTipPersona, String codLicencia, String rol, Long idUsuario, BigDecimal page);

    ConductoresEntity obtenerConductorPorCodEmpleadoOCorreoYPassword(String usuario, String password);
    ConductoresEntity obtenerConductorPorCorreo(String mail);

}
