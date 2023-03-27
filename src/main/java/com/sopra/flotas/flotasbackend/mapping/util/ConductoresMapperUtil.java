package com.sopra.flotas.flotasbackend.mapping.util;

import com.sopra.albia.persistence.entity.ConductorespropiedadvehiculosEntity;
import com.sopra.flotas.flotasbackend.contract.generated.models.Vehiculo;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConductoresMapperUtil {

    @Named("conductoresVehiculosToVehiculo")
    public List<Vehiculo> conductoresVehiculosToVehiculo(List<ConductorespropiedadvehiculosEntity> conductores){
        if(conductores == null){
            return null;
        }
        List<Vehiculo> result = new ArrayList<>();
        for (ConductorespropiedadvehiculosEntity conductorespropiedadvehiculosEntity: conductores) {
            result.add(Vehiculo.builder().idVehiculo(conductorespropiedadvehiculosEntity.getVehiculosByIdVehiculo().getIdVehiculo())
                    .sMatricula(conductorespropiedadvehiculosEntity.getVehiculosByIdVehiculo().getsMatricula()).build());
        }
        return result;
    }

}
