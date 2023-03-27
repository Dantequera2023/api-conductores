package com.sopra.flotas.flotasbackend.contract.generated.src.main.java.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sopra.flotas.flotasbackend.security.utilidad.JwtUtililidad;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class TokenTest {


    @Autowired
    private JwtUtililidad jwtUtililidad;


    @Test
    @Disabled
    void shouldToken() throws IOException {
        String token = jwtUtililidad.generateToken(1L,"cod-1","ADMIN");
        Long conductor = jwtUtililidad.obtenerIdConductorDeToken(token);
        MatcherAssert.assertThat("ConductorCorrecto",Long.valueOf(1).equals(conductor ));
    }

    @Test
    @Disabled
    void validToken() throws JsonProcessingException {
        String token = jwtUtililidad.generateToken(1L,"cod-1","ADMIN");
        MatcherAssert.assertThat("Validar Token", jwtUtililidad.validarToken(token));
    }

}
