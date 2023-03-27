package com.sopra.flotas.flotasbackend.service;


import com.sopra.flotas.flotasbackend.contract.generated.models.Conductor;
import com.sopra.flotas.flotasbackend.contract.generated.models.ConductoresDescripciones;
import com.sopra.flotas.flotasbackend.contract.generated.models.ConductoresDescripcionesPage;
import com.sopra.flotas.flotasbackend.contract.generated.models.InlineResponse200;
import com.sopra.flotas.flotasbackend.contract.generated.models.InlineResponse2001;
import com.sopra.flotas.flotasbackend.contract.generated.models.Licencias;
import com.sopra.flotas.flotasbackend.contract.generated.models.UsuarioLogin;
import com.sopra.flotas.flotasbackend.contract.generated.models.UsuarioPassword;
import com.sopra.flotas.flotasbackend.contract.generated.models.Vehiculo;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.util.List;

public interface ConductoresService {


    List<ConductoresDescripcionesPage> obtenerConductores(BigDecimal page);

    List<ConductoresDescripcionesPage> obtenerConductoresPorIDMasDatos(Conductor conductores, List<String> listCodCentro, List<String> listCodTerritorial,
                                                                       List<BigDecimal> listTipPersona, String codLicencia, BigDecimal page);

    ConductoresDescripciones obtenerConductorIdConductorYCodCentro(Conductor conductores);

    InlineResponse200 resetPassword(UsuarioPassword usuarioPassword);

    InlineResponse2001 sendMail(String email, String app) throws MessagingException;

    List<ConductoresDescripciones> insertarConductores(ConductoresDescripciones conductores);

    List<Licencias> obtenerLincenciasConductor(BigDecimal idConductor);


    List<Vehiculo> obtenerVehiculosConductor(BigDecimal idConductor);


    Conductor obtenerConductorCodUsuarioMailPassword(UsuarioLogin usuarioLogin);


}
