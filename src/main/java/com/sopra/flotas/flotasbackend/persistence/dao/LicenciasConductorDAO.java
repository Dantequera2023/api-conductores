package com.sopra.flotas.flotasbackend.persistence.dao;

import com.sopra.albia.persistence.entity.LicenciasconductorEntity;
import com.sopra.albia.persistence.entity.LicenciasconductorEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenciasConductorDAO extends JpaRepository<LicenciasconductorEntity, LicenciasconductorEntityPK> {
}
