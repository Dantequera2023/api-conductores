package com.sopra.flotas.flotasbackend.controller;

import com.sopra.flotas.flotasbackend.contract.generated.api.AccesoApi;
import com.sopra.flotas.flotasbackend.contract.generated.models.Conductor;
import com.sopra.flotas.flotasbackend.contract.generated.models.ConductorAcceso;
import com.sopra.flotas.flotasbackend.contract.generated.models.InlineResponse200;
import com.sopra.flotas.flotasbackend.contract.generated.models.InlineResponse2001;
import com.sopra.flotas.flotasbackend.contract.generated.models.UsuarioLogin;
import com.sopra.flotas.flotasbackend.contract.generated.models.UsuarioPassword;
import com.sopra.flotas.flotasbackend.security.utilidad.JwtUtililidad;
import com.sopra.flotas.flotasbackend.service.ConductoresService;
import com.sopra.flotas.flotasbackend.service.TiposPersonasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AccesoApiController implements AccesoApi {


    @Autowired
    private ConductoresService conductoresService;

    @Autowired
    private TiposPersonasService tiposPersonasService;

    @Autowired
    private JwtUtililidad jwtUtililidad;

    @Value("${app.messages.server.error}")
    private String serverError;

    @Override
    public ResponseEntity<ConductorAcceso> accesoPost(UsuarioLogin usuarioLogin) {
        try {
            Conductor conductor = conductoresService.obtenerConductorCodUsuarioMailPassword(usuarioLogin);
            if(conductor != null){
                String rol = tiposPersonasService.obtenerRolTipoPerson(conductor.getIdTipopersona().longValue());
                return new ResponseEntity<>(ConductorAcceso.builder().conductor(conductor).token(jwtUtililidad.generateToken(conductor.getIdConductor().longValueExact(),conductor.getCodEmpleado(),rol)).build(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error(serverError, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<InlineResponse200> recoverPost(UsuarioPassword usuarioPassword) {
        try {
            InlineResponse200 response = conductoresService.resetPassword(usuarioPassword);
            if(response != null){
                return new ResponseEntity<>(response, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error(serverError, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<InlineResponse2001> sendmailGet(String email, String app) {
        try {
            InlineResponse2001 response = conductoresService.sendMail(email, app);
            if(response != null){
                return new ResponseEntity<>(response, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error(serverError, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
