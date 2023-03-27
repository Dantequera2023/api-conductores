package com.sopra.flotas.flotasbackend.persistence.entity.projection;

import com.sopra.albia.persistence.entity.CentrosEntity;
import com.sopra.albia.persistence.entity.ConductoresEntity;
import com.sopra.albia.persistence.entity.ConductorescentrosEntity;
import com.sopra.albia.persistence.entity.TerritorialesEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;

@Data
public class ConductorFilterResultProjection extends ConductoresEntity {


    public ConductorFilterResultProjection(ConductoresEntity conductores, CentrosEntity centro, TerritorialesEntity territoriales){
        super();
        BeanUtils.copyProperties(conductores,this);
        ConductorescentrosEntity conductorescentrosEntity = new ConductorescentrosEntity();
        conductorescentrosEntity.setConductoresByIdConductor(conductores);
        conductorescentrosEntity.setCentrosByCodCentro(centro);
       super.setConductorescentrosByIdConductor(Arrays.asList(conductorescentrosEntity));
        super.setTerritorialesByCodTerritorial(territoriales);
    }

}
