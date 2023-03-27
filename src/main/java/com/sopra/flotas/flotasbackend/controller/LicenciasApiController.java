package com.sopra.flotas.flotasbackend.controller;


import com.sopra.flotas.flotasbackend.contract.generated.api.LicenciasConductorApi;
import com.sopra.flotas.flotasbackend.contract.generated.models.Estado;
import com.sopra.flotas.flotasbackend.contract.generated.models.Licencias;
import com.sopra.flotas.flotasbackend.security.model.DetallesUsuario;
import com.sopra.flotas.flotasbackend.service.LicenciasService;
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
public class LicenciasApiController implements LicenciasConductorApi {
    private final LicenciasService licenciasService;
    @Value("${app.messages.server.error}")
    private String serverError;

    @Autowired
    public LicenciasApiController(LicenciasService licenciasService) {

        this.licenciasService = licenciasService;
    }

    public ResponseEntity<List<Licencias>> licenciasconductorFilterGet(BigDecimal idConductor, String idLicencia, String dtFechaexpedicion, String dtFecharenovacion, String stObserviaciones, BigDecimal numPuntos, String stLimitaciones, String dtFechaultimamodificacion, String codEjecutor, String codLicencia) {
        try {
            List<Licencias> licencias = licenciasService.obtenerLicenciasPorIDMasDatos(new Licencias(idConductor, idLicencia, dtFechaexpedicion, dtFecharenovacion, stObserviaciones, numPuntos, stLimitaciones, dtFechaultimamodificacion, codEjecutor, codLicencia, Estado.builder().build()));

            if (licencias.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(licencias, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(serverError, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Licencias>> licenciasconductorGet() {
        try {
            List<Licencias> licencias = licenciasService.obtenerLicencias();

            if (licencias.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(licencias, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(serverError, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<List<Licencias>> licenciasconductorDelete(Licencias licencias) {
        try {
            List<Licencias> licenciasResult = licenciasService.deleteLicenciasAndRecuperar(licencias);
            if (licenciasResult.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(licenciasResult, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(serverError, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Licencias>> licenciasconductorPost(Licencias body) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DetallesUsuario detalle = (DetallesUsuario)authentication.getPrincipal();
        body.setCodEjecutor(detalle.getCodEmpleado());
        List<Licencias> licencias = licenciasService.insertarLicenciasAndRecuperar(body);

        if (licencias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(licencias, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<Licencias>> licenciasconductorPut(Licencias body) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            DetallesUsuario detalle = (DetallesUsuario)authentication.getPrincipal();
            body.setCodEjecutor(detalle.getCodEmpleado());
            List<Licencias> licencias = licenciasService.modificarLicencia(body);
            if (body.getIdLicencia() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (licencias.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(licencias, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(serverError, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
