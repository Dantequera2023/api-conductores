package com.sopra.flotas.flotasbackend.service.impl;

import com.sopra.albia.persistence.entity.ConductoresEntity;
import com.sopra.albia.persistence.entity.LicenciasconductorEntity;
import com.sopra.albia.persistence.entity.LicenciasconductorEntityPK;
import com.sopra.flotas.flotasbackend.contract.generated.models.Licencias;
import com.sopra.flotas.flotasbackend.mapping.LicenciasMapper;
import com.sopra.flotas.flotasbackend.persistence.dao.ConductoresDAO;
import com.sopra.flotas.flotasbackend.persistence.dao.LicenciasConductorDAO;
import com.sopra.flotas.flotasbackend.service.LicenciasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class LicenciasServiceImpl implements LicenciasService {


    private final LicenciasMapper mapper;

    private final LicenciasConductorDAO licenciasConductorDAO;

    @Autowired
    private ConductoresDAO conductoresDAO;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    public LicenciasServiceImpl(final LicenciasMapper mapper, final LicenciasConductorDAO licenciasConductorDAO) {
        this.mapper = mapper;
        this.licenciasConductorDAO = licenciasConductorDAO;
    }

    public List<Licencias> obtenerLicencias() {
        List<LicenciasconductorEntity> listLicencias = licenciasConductorDAO.findAll();
        return listLicencias.stream().map(mapper::mapLicencias).collect(Collectors.toList());
    }
    @Transactional
    public List<Licencias> obtenerLicenciasPorIDMasDatos(Licencias licencias) {
        List<LicenciasconductorEntity> listaLicencias = licenciasConductorDAO.findAll(Example.of(mapper.mapLicencias2(licencias)),Sort.by(Sort.Direction.DESC,"idLicencia"));
        return listaLicencias.stream().map(mapper::mapLicencias).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Licencias> insertarLicenciasAndRecuperar(Licencias licencias) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        licencias.setDtFechaultimamodificacion(formatter.format(LocalDateTime.now()));
        LicenciasconductorEntity licencia = mapper.mapLicencias2(licencias);
        ConductoresEntity conductor = conductoresDAO.getOne(licencia.getConductoresByIdConductor().getIdConductor());
        licencia.setConductoresByIdConductor(conductor);
        List<Licencias> licenciasResult = this.obtenerLincenciasIdConductor(licencias.getIdConductor());
        if(licencias.getIdLicencia() == null) {
            if (licenciasResult.isEmpty()) {
                licencia.setIdLicencia(String.valueOf(1L));
            } else {
                licencia.setIdLicencia(licenciasResult.get(0).getIdLicencia() + 1L);
            }
        }
        return this.obtenerLincenciasIdConductor(licencias.getIdConductor());
    }
    @Transactional
    public List<Licencias> modificarLicencia(Licencias licencias) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        licencias.setDtFechaultimamodificacion(formatter.format(LocalDateTime.now()));
        LicenciasconductorEntity licencia = mapper.mapLicencias2(licencias);
        ConductoresEntity conductor = conductoresDAO.getOne(licencia.getConductoresByIdConductor().getIdConductor());
        licencia.setConductoresByIdConductor(conductor);
        licenciasConductorDAO.save(licencia);
        return this.obtenerLincenciasIdConductor(licencias.getIdConductor());
    }

    @Transactional
    public List<Licencias> obtenerLincenciasIdConductor(BigDecimal idConductor){
        return this.obtenerLicenciasPorIDMasDatos(Licencias.builder().idConductor(idConductor).build());
    }


    @Override
    @Transactional
    public List<Licencias> deleteLicenciasAndRecuperar(Licencias licencias) {
        LicenciasconductorEntityPK lincenciaPk = new LicenciasconductorEntityPK();
        lincenciaPk.setIdLicencia(licencias.getIdLicencia());
        lincenciaPk.setIdConductor(licencias.getIdConductor().longValueExact());
        licenciasConductorDAO.deleteById(lincenciaPk);
        return this.obtenerLincenciasIdConductor(licencias.getIdConductor());
    }


}
