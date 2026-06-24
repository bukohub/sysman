package com.sysman.orden.infrastructure.persistence.repository;

import com.sysman.orden.infrastructure.persistence.entity.OrdenHistoricoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdenHistoricoJpaRepository extends JpaRepository<OrdenHistoricoEntity, Long> {

    List<OrdenHistoricoEntity> findByOrdenIdOrderByFechaCambioDesc(Long ordenId);

    @Query("SELECT DISTINCT h.usuario FROM OrdenHistoricoEntity h "
            + "WHERE UPPER(h.usuario) LIKE UPPER(CONCAT('%', :texto, '%')) "
            + "ORDER BY h.usuario")
    List<String> buscarUsuariosPorTexto(@Param("texto") String texto, Pageable pageable);
}
