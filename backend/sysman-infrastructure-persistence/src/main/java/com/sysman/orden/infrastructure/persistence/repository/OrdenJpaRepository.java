package com.sysman.orden.infrastructure.persistence.repository;

import com.sysman.orden.infrastructure.persistence.entity.OrdenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrdenJpaRepository extends JpaRepository<OrdenEntity, Long>, JpaSpecificationExecutor<OrdenEntity> {
}
