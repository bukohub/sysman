package com.sysman.orden.infrastructure.persistence.repository;

import com.sysman.orden.infrastructure.persistence.entity.ClienteEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Long> {

    @Query("SELECT c FROM ClienteEntity c "
            + "WHERE UPPER(c.nombre) LIKE UPPER(CONCAT('%', :texto, '%')) "
            + "OR UPPER(c.numeroIdentificacion) LIKE UPPER(CONCAT('%', :texto, '%')) "
            + "ORDER BY c.nombre")
    List<ClienteEntity> buscarPorTexto(@Param("texto") String texto, Pageable pageable);
}
