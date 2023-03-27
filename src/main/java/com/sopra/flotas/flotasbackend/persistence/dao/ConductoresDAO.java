package com.sopra.flotas.flotasbackend.persistence.dao;

import com.sopra.albia.persistence.entity.ConductoresEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConductoresDAO extends JpaRepository<ConductoresEntity, Long>, ConductoresCustomDAO {

}
