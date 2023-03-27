package com.sopra.flotas.flotasbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.fasterxml.jackson.databind.ObjectMapper;




class CompaniasServiceTest {

   /* @InjectMocks
    private CompaniasServiceImpl service;

    @Mock
    private CompaniaMapper mapper;

    @Mock
    private CompaniaDAO companiaDAO;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindAll() {
        // given
        final Integer idCompania = 1;
        final String desc = "prueba1";

        final BckCompaniasEntity compEnt = new BckCompaniasEntity();
        compEnt.setIdCompania(idCompania);
        compEnt.setDescripcionCorta(desc);

        final Compania compania = new Compania();

        compania.setIdCompania(idCompania);
        compania.setDescripcionCorta(desc);

        doReturn(Arrays.asList(compEnt)).when(companiaDAO).findAll();

        doReturn(compania).when(mapper).mapCompania(compEnt);

        // when
        final List<Compania> companias = service.obtenerCompanias();

        // then
        assertNotNull(companias);
        verify(companiaDAO, times(1)).findAll();
        assertNotNull(companias);
        assertEquals(1, companias.size());
        assertTrue(companias.contains(compania));

    }*/

}
