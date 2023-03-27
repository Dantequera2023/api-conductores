package com.sopra.flotas.flotasbackend.controller;


import com.sopra.flotas.flotasbackend.contract.generated.api.TiposPersonasApi;
import com.sopra.flotas.flotasbackend.contract.generated.models.TiposPersonas;
import com.sopra.flotas.flotasbackend.service.TiposPersonasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;


@RestController
@Slf4j
public class TiposPersonasController implements TiposPersonasApi {
    private final TiposPersonasService tiposPersonasService;
    @Value("${app.messages.server.error}")
    private String serverError;

    @Autowired
    public TiposPersonasController(TiposPersonasService tiposPersonasService) {

        this.tiposPersonasService = tiposPersonasService;
    }

    @Override
    public ResponseEntity<List<TiposPersonas>> tipospersonasGet() {
        try {
            List<TiposPersonas> tiposPersonas = tiposPersonasService.obtenerTipoPersona();

            if (tiposPersonas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(tiposPersonas, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(serverError, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<List<TiposPersonas>> tipospersonasFilterGet(BigDecimal idTipopersona, String descTipopersona) {
        try {
            List<TiposPersonas> tiposPersonas = tiposPersonasService.obtenerTipoPersonaPorIDMasDatos(new TiposPersonas(idTipopersona, descTipopersona));

            if (tiposPersonas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(tiposPersonas, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(serverError, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<List<TiposPersonas>> tipospersonasPost(TiposPersonas body) {
        try {
            List<TiposPersonas> tiposPersonas = tiposPersonasService.insertarTiposPersona(body);

            if (tiposPersonas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(tiposPersonas, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(serverError, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
