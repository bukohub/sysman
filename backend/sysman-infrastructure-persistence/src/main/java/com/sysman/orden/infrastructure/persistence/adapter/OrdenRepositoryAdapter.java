package com.sysman.orden.infrastructure.persistence.adapter;

import com.sysman.orden.application.port.out.OrdenRepositoryPort;
import com.sysman.orden.application.query.FiltroOrdenQuery;
import com.sysman.orden.application.query.PaginaResultado;
import com.sysman.orden.domain.exception.ClienteNoEncontradoException;
import com.sysman.orden.domain.exception.OrdenNoEncontradaException;
import com.sysman.orden.domain.model.Cliente;
import com.sysman.orden.domain.model.Orden;
import com.sysman.orden.domain.model.OrdenHistorico;
import com.sysman.orden.infrastructure.persistence.entity.ClienteEntity;
import com.sysman.orden.infrastructure.persistence.entity.OrdenEntity;
import com.sysman.orden.infrastructure.persistence.entity.OrdenHistoricoEntity;
import com.sysman.orden.infrastructure.persistence.mapper.ClienteEntityMapper;
import com.sysman.orden.infrastructure.persistence.mapper.OrdenEntityMapper;
import com.sysman.orden.infrastructure.persistence.mapper.OrdenHistoricoEntityMapper;
import com.sysman.orden.infrastructure.persistence.repository.ClienteJpaRepository;
import com.sysman.orden.infrastructure.persistence.repository.OrdenHistoricoJpaRepository;
import com.sysman.orden.infrastructure.persistence.repository.OrdenJpaRepository;
import com.sysman.orden.infrastructure.persistence.specification.OrdenSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrdenRepositoryAdapter implements OrdenRepositoryPort {

    private final OrdenJpaRepository ordenJpaRepository;
    private final ClienteJpaRepository clienteJpaRepository;
    private final OrdenHistoricoJpaRepository ordenHistoricoJpaRepository;
    private final OrdenEntityMapper ordenEntityMapper;
    private final OrdenHistoricoEntityMapper ordenHistoricoEntityMapper;
    private final ClienteEntityMapper clienteEntityMapper;

    public OrdenRepositoryAdapter(OrdenJpaRepository ordenJpaRepository,
                                   ClienteJpaRepository clienteJpaRepository,
                                   OrdenHistoricoJpaRepository ordenHistoricoJpaRepository,
                                   OrdenEntityMapper ordenEntityMapper,
                                   OrdenHistoricoEntityMapper ordenHistoricoEntityMapper,
                                   ClienteEntityMapper clienteEntityMapper) {
        this.ordenJpaRepository = ordenJpaRepository;
        this.clienteJpaRepository = clienteJpaRepository;
        this.ordenHistoricoJpaRepository = ordenHistoricoJpaRepository;
        this.ordenEntityMapper = ordenEntityMapper;
        this.ordenHistoricoEntityMapper = ordenHistoricoEntityMapper;
        this.clienteEntityMapper = clienteEntityMapper;
    }

    @Override
    public Orden guardar(Orden orden) {
        ClienteEntity clienteRef = clienteJpaRepository.findById(orden.getCliente().getId())
                .orElseThrow(() -> new ClienteNoEncontradoException(orden.getCliente().getId()));

        OrdenEntity nuevaEntidad = ordenEntityMapper.toNewEntity(orden, clienteRef);
        OrdenEntity guardada = ordenJpaRepository.save(nuevaEntidad);

        List<OrdenHistoricoEntity> historicoEntities = orden.getHistorico().stream()
                .map(h -> ordenHistoricoEntityMapper.toNewEntity(h, guardada.getId()))
                .collect(Collectors.toList());
        ordenHistoricoJpaRepository.saveAll(historicoEntities);

        return buscarPorId(guardada.getId())
                .orElseThrow(() -> new OrdenNoEncontradaException(guardada.getId()));
    }

    @Override
    public Optional<Orden> buscarPorId(Long id) {
        return ordenJpaRepository.findById(id).map(entity -> {
            Cliente cliente = clienteEntityMapper.toDomain(entity.getCliente());
            List<OrdenHistorico> historico = ordenHistoricoJpaRepository.findByOrdenIdOrderByFechaCambioDesc(id)
                    .stream()
                    .map(ordenHistoricoEntityMapper::toDomain)
                    .collect(Collectors.toList());
            return ordenEntityMapper.toDomain(entity, cliente, historico);
        });
    }

    @Override
    public PaginaResultado<Orden> buscarConFiltros(FiltroOrdenQuery filtro, int pagina, int tamanioPagina) {
        Specification<OrdenEntity> spec = Specification
                .where(OrdenSpecifications.conEstado(filtro.estado()))
                .and(OrdenSpecifications.conFechaCreacionDesde(filtro.fechaInicio()))
                .and(OrdenSpecifications.conFechaCreacionHasta(filtro.fechaFin()));

        PageRequest pageRequest = PageRequest.of(pagina, tamanioPagina, Sort.by("fechaCreacion").descending());
        Page<OrdenEntity> page = ordenJpaRepository.findAll(spec, pageRequest);

        List<Orden> contenido = page.getContent().stream()
                .map(entity -> ordenEntityMapper.toDomain(entity, clienteEntityMapper.toDomain(entity.getCliente()), List.of()))
                .collect(Collectors.toList());

        return new PaginaResultado<>(contenido, pagina, tamanioPagina, page.getTotalElements(), page.getTotalPages());
    }
}
