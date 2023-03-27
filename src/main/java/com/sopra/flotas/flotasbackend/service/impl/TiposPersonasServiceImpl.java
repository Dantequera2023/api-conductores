package com.sopra.flotas.flotasbackend.service.impl;

import com.sopra.albia.persistence.entity.TipospersonasEntity;
import com.sopra.flotas.flotasbackend.contract.generated.models.TiposPersonas;
import com.sopra.flotas.flotasbackend.mapping.TiposPersonasMapper;
import com.sopra.flotas.flotasbackend.persistence.dao.TiposPersonasDAO;
import com.sopra.flotas.flotasbackend.service.TiposPersonasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TiposPersonasServiceImpl implements TiposPersonasService {


    private final TiposPersonasMapper mapper;

    private final TiposPersonasDAO tiposPersonasDAO;

    @Autowired
    public TiposPersonasServiceImpl(final TiposPersonasMapper mapper, final TiposPersonasDAO tiposPersonasDAO) {
        this.mapper = mapper;
        this.tiposPersonasDAO = tiposPersonasDAO;
    }

    @Override
    public List<TiposPersonas> obtenerTipoPersona() {
        List<TipospersonasEntity> tiposPersonas =  tiposPersonasDAO.findAll();
        return tiposPersonas.stream().map(mapper::mapTiposPersonas).collect(Collectors.toList());
    }

    @Override
    public List<TiposPersonas> obtenerTipoPersonaPorIDMasDatos(TiposPersonas tiposPersonas) {
        List<TipospersonasEntity> listaTiposPersonas =  tiposPersonasDAO.findAll(Example.of(mapper.mapTiposPersonas2(tiposPersonas)));
        return listaTiposPersonas.stream().map(mapper::mapTiposPersonas).collect(Collectors.toList());
    }


    @Override
    public List<TiposPersonas> insertarTiposPersona(TiposPersonas tiposPersonas) {
        List<TipospersonasEntity> listaTiposPersonas = new ArrayList<>();
        listaTiposPersonas.add(tiposPersonasDAO.save(mapper.mapTiposPersonas2(mapper.mapTiposPersonas3(tiposPersonas))));
        return listaTiposPersonas.stream().map(mapper::mapTiposPersonas).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public String obtenerRolTipoPerson(Long idTipoPersona) {
        TipospersonasEntity tipoPersona = tiposPersonasDAO.getOne(idTipoPersona);
        return tipoPersona.getRol();
    }
}


