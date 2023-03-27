package com.sopra.flotas.flotasbackend.service;


import com.sopra.flotas.flotasbackend.contract.generated.models.TiposPersonas;

import java.util.List;

public interface TiposPersonasService {

   List<TiposPersonas> obtenerTipoPersona();
   List< TiposPersonas> obtenerTipoPersonaPorIDMasDatos(TiposPersonas tiposPersonas);
   List<TiposPersonas>insertarTiposPersona(TiposPersonas tiposPersonas);

   String obtenerRolTipoPerson(Long idTipoPersona);




}
