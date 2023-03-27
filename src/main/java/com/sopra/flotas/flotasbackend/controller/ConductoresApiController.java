package com.sopra.flotas.flotasbackend.controller;


import com.sopra.flotas.flotasbackend.contract.generated.api.ConductoresApi;
import com.sopra.flotas.flotasbackend.contract.generated.models.Conductor;
import com.sopra.flotas.flotasbackend.contract.generated.models.ConductoresDescripciones;
import com.sopra.flotas.flotasbackend.contract.generated.models.ConductoresDescripcionesPage;
import com.sopra.flotas.flotasbackend.contract.generated.models.Licencias;
import com.sopra.flotas.flotasbackend.contract.generated.models.Vehiculo;
import com.sopra.flotas.flotasbackend.security.model.DetallesUsuario;
import com.sopra.flotas.flotasbackend.service.ConductoresService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;


@RestController
@Slf4j
public class ConductoresApiController implements ConductoresApi {
    private final ConductoresService conductoresService;
    @Value("${app.messages.server.error}")
    private String serverError;

    @Autowired
    public ConductoresApiController(ConductoresService conductoresService) {

        this.conductoresService = conductoresService;
    }


    @Override
    public ResponseEntity<List<ConductoresDescripcionesPage>> conductoresGet(BigDecimal page) {
        try {
            List<ConductoresDescripcionesPage> conductores = conductoresService.obtenerConductores(page);

            if (conductores.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(conductores, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(serverError, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<ConductoresDescripcionesPage>> conductoresFilterGet(BigDecimal idConductor, List<BigDecimal> idTipopersona, List<String> codCentro, List<String> codTerritorial, String codEmpleado, String stDireccion, String codNacionalidad, BigDecimal idTipoidentificacion, String codIdentificacion, String stTelefono, String stTelefonooperaciones, BigDecimal idEstado, String dtFechaultimamodificacion, String codEjecutor, String stNombre, String stApellidos, String stLicencia, BigDecimal page) {
        try {
            List<ConductoresDescripcionesPage> conductores = conductoresService.obtenerConductoresPorIDMasDatos(new Conductor(idConductor, null, null, null, codEmpleado, stDireccion, codNacionalidad, idTipoidentificacion, codIdentificacion, stTelefono, stTelefonooperaciones, idEstado, dtFechaultimamodificacion, codEjecutor, stNombre, stApellidos), codCentro, codTerritorial, idTipopersona, stLicencia, page);

            if (conductores.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(conductores, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(serverError, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ConductoresDescripciones> conductoresIdConductorGet(BigDecimal idConductor, String codCentro) {
        ConductoresDescripciones conductor = conductoresService.obtenerConductorIdConductorYCodCentro(Conductor.builder().idConductor(idConductor).build());
        if (conductor == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(conductor, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<Licencias>> conductoresIdConductorLicenciasGet(BigDecimal idConductor) {
        List<Licencias> licencias = conductoresService.obtenerLincenciasConductor(idConductor);
        if (licencias == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(licencias, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<Vehiculo>> conductoresIdConductorVehiculosGet(BigDecimal idConductor) {
        List<Vehiculo> vehiculos = conductoresService.obtenerVehiculosConductor(idConductor);
        if (vehiculos == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(vehiculos, HttpStatus.OK);
        }
    }


    @Override
    public ResponseEntity<List<ConductoresDescripciones>> conductoresPost(ConductoresDescripciones body) {


        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            DetallesUsuario detalle = (DetallesUsuario)authentication.getPrincipal();
            body.setCodEjecutor(detalle.getCodEmpleado());
            List<ConductoresDescripciones> conductores = conductoresService.insertarConductores(body);


            if (conductores.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(conductores, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(serverError, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<ConductoresDescripciones>> conductoresPut(ConductoresDescripciones body) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            DetallesUsuario detalle = (DetallesUsuario)authentication.getPrincipal();
            body.setCodEjecutor(detalle.getCodEmpleado());
            if (body.getIdConductor() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                List<ConductoresDescripciones> conductores = conductoresService.insertarConductores(body);
                if (conductores.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(conductores, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            log.error(serverError, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}


