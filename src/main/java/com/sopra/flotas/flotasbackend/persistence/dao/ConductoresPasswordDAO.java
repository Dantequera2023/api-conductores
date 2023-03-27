package com.sopra.flotas.flotasbackend.persistence.dao;

import com.sopra.albia.persistence.entity.ConductoresEntity;
import com.sopra.albia.persistence.entity.ConductorespwdsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConductoresPasswordDAO extends JpaRepository<ConductorespwdsEntity, Long> {

}
