package com.sopra.flotas.flotasbackend.contract.generated.src.main.java.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



class CompaniasApiControllerTest {

   /* @InjectMocks
    private CompaniasApiController controller;

    @Mock
    private CompaniasService companiasService;

    private final Integer idCompania = 1;
    private final String desc = "prueba1";

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindAll() {
        // given
        final Compania compania = new Compania();

        compania.setIdCompania(idCompania);
        compania.setDescripcionCorta(desc);

        doReturn(Arrays.asList(compania)).when(companiasService).obtenerCompanias();

        // when
        final ResponseEntity<List<Compania>> response = controller.companiasGet();
        final List<Compania> companias = response.getBody();

        // then
        assertNotNull(companias);
        verify(companiasService, times(1)).obtenerCompanias();
        assertNotNull(companias);
        assertEquals(1, companias.size());
        assertTrue(companias.contains(compania));
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void shouldGiveError() {
        doThrow(new IllegalStateException("Error occurred")).when(companiasService).obtenerCompanias();
        final ResponseEntity<List<Compania>> response = controller.companiasGet();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }*/

}
