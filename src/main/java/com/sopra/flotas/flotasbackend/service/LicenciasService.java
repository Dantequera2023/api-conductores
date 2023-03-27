package com.sopra.flotas.flotasbackend.service;


import com.sopra.flotas.flotasbackend.contract.generated.models.Licencias;

import java.math.BigDecimal;
import java.util.List;


public interface LicenciasService {

   List<Licencias> obtenerLicencias();
   List<Licencias> obtenerLicenciasPorIDMasDatos(Licencias licencias);
   List<Licencias> insertarLicenciasAndRecuperar(Licencias licencias);

   List<Licencias> modificarLicencia(Licencias licencias);

   List<Licencias> deleteLicenciasAndRecuperar(Licencias licencias);

   List<Licencias> obtenerLincenciasIdConductor(BigDecimal idConductor);

}
