# Arquitectura - Sistema de Gestion de Ordenes Operativas (Sysman)

## 1. Arquitectura propuesta

El sistema se implementa con **arquitectura hexagonal (ports & adapters)** sobre **Spring Boot 3 / Java 21**, organizada como **5 modulos Maven** que materializan fisicamente las fronteras de capas:

- `sysman-domain`: modelo de negocio puro (`Orden`, `Cliente`, `OrdenHistorico`, `EstadoOrden`, `TipoOrden`, `ValidadorTransicionEstado`, excepciones de dominio). Sin dependencias de framework.
- `sysman-application`: casos de uso (`CrearOrdenUseCase`, `ConsultarOrdenUseCase`, `ActualizarEstadoOrdenUseCase`, `ListarOrdenesUseCase`) y puertos de salida (`OrdenRepositoryPort`, `ClienteRepositoryPort`, `ActualizarEstadoOrdenPort`). Aqui vive el boundary `@Transactional`.
- `sysman-infrastructure-persistence`: adaptadores Oracle/JPA, migraciones Flyway y el paquete PL/SQL.
- `sysman-infrastructure-rest`: controllers REST v1, DTOs, manejo global de excepciones.
- `sysman-bootstrap`: modulo ejecutable que ensambla todos los adaptadores.

Flujo de una peticion tipica (`PUT /api/v1/orden/{id}/estado`): `OrdenController` valida el DTO -> mapea a `ActualizarEstadoOrdenCommand` -> `ActualizarEstadoOrdenService` (transaccional) valida la transicion en Java (defensa temprana) -> invoca `ActualizarEstadoOrdenPort`, implementado por `ActualizarEstadoOrdenJdbcAdapter`, que ejecuta el procedimiento PL/SQL `orden_pkg.actualizar_estado_orden` via `SimpleJdbcCall` -> se relee la orden actualizada y se devuelve como `OrdenResponse`.

El frontend (Next.js, App Router) consume la API REST: Server Components para listado/detalle (SSR con `fetch` directo al backend) y Client Components para los formularios de creacion y cambio de estado.

## 2. Justificacion tecnica

- **Hexagonal multi-modulo**: el modulo `sysman-domain` no declara ninguna dependencia de Spring/JPA, por lo que es fisicamente imposible que una regla de negocio termine acoplada a un detalle de infraestructura — el build falla si se intenta.
- **PL/SQL para la transicion de estado** (en vez de hacerlo todo en JPA): la transicion de estado es la operacion critica concurrente del sistema. Ejecutarla como un procedimiento atomico en Oracle permite usar `SELECT ... FOR UPDATE NOWAIT` de forma controlada y mantener la logica de "verificar version + validar transicion + insertar historico + actualizar estado" como una sola unidad indivisible, sin viajes de red adicionales entre el backend y la base de datos.
- **Particionamiento mensual**: a 1M ordenes/mes, particionar `ORDEN` y `ORDEN_HISTORICO` por rango mensual de fecha mantiene cada particion en un tamaño manejable (~1M filas) y permite purgar/archivar datos antiguos con `DROP PARTITION` sin impactar las particiones activas.

## 3. Estrategia de manejo de transacciones

El boundary transaccional vive en la capa de aplicacion (`*Service`), nunca en el controller ni en un adaptador individual:

- `CrearOrdenService.ejecutar()`: `@Transactional(rollbackFor = Exception.class)` — inserta la orden y su primer registro de historico (estado `CREADA`) atomicamente via JPA estandar.
- `ActualizarEstadoOrdenService.ejecutar()`: `@Transactional(rollbackFor = Exception.class)` — la llamada al procedimiento PL/SQL (via `SimpleJdbcCall`) participa en la misma transaccion JDBC que el resto del metodo. El procedimiento **no hace COMMIT**; el commit/rollback es responsabilidad exclusiva del backend.
- Lecturas (`ConsultarOrdenUseCase`, `ListarOrdenesUseCase`): `@Transactional(readOnly = true)`, permite a Hibernate evitar dirty checking innecesario.

## 4. Manejo de concurrencia

Se combinan dos mecanismos sin que compitan entre si:

- **Optimistic locking** (`@Version` en JPA) como mecanismo general — gobierna cualquier escritura hecha via `repository.save()`.
- **Lock pesimista puntual** (`SELECT ... FOR UPDATE NOWAIT`), usado **unicamente** dentro del procedimiento PL/SQL `actualizar_estado_orden`, durante la duracion de esa transaccion corta. El campo `estado` de una orden **solo** se modifica a traves de este procedimiento, nunca via `repository.save()` — esto evita que Hibernate y el procedimiento incrementen `version` de forma inconsistente.
- El cliente debe enviar la `version` que conoce en `PUT /orden/{id}/estado`. Si no coincide con la version real en BD, el procedimiento lanza `ORA-20003`, traducido a `ConflictoConcurrenciaException` -> **HTTP 409 Conflict**. El cliente debe recargar la orden y reintentar.

## 5. Estrategia de indexacion en Oracle

- `ORDEN` y `ORDEN_HISTORICO` particionadas por `RANGE (fecha_creacion / fecha_cambio) INTERVAL (1 MES)`.
- Indices **globales** (no locales) sobre `(estado, fecha_creacion)`, `(fecha_creacion DESC)` y `(cliente_id)` en `ORDEN`, y `(orden_id, fecha_cambio DESC)` en `ORDEN_HISTORICO` — necesarios porque los filtros del listado (`estado` + rango de fechas) cruzan particiones; un indice local obligaria a escanear el indice de cada particion candidata.
- **Trade-off documentado**: los indices globales encarecen el mantenimiento de `DROP PARTITION` (requieren `UPDATE INDEXES` o rebuild). Se acepta este costo porque ocurre en una ventana de mantenimiento controlada (purga mensual), mientras que las consultas de listado ocurren constantemente en el camino caliente.
- Politica de alto volumen: retencion de 24 meses en caliente; particiones mas antiguas se exportan (Data Pump) y se eliminan con `DROP PARTITION`.

## 6. Estrategia de versionamiento de API

Versionamiento por **URI** (`/api/v1/orden`). Los controllers viven en el paquete `infrastructure.rest.v1`. Si en el futuro se requiere una v2 incompatible, se crea un paquete `infrastructure.rest.v2` con sus propios DTOs y controllers, reutilizando los mismos casos de uso de la capa de aplicacion — el versionamiento de API nunca se propaga a `application` ni a `domain`.

## 7. Estrategia de logging y auditoria

Se separan dos conceptos deliberadamente:

- **Logging tecnico**: Logback + `logstash-logback-encoder` (JSON estructurado en los perfiles `docker`/`prod`), con un `CorrelationIdFilter` que genera/propaga el header `X-Correlation-Id` y lo coloca en el MDC. Util para trazabilidad operativa, debugging y correlacion entre componentes — rota con politicas de dias/semanas.
- **Auditoria de negocio**: la tabla `ORDEN_HISTORICO` es la fuente de verdad — quien cambio que estado, cuando y por que. Es transaccional, consultable por el propio negocio via la API, y no se purga con la misma politica que los logs tecnicos (retencion de 24 meses en caliente, luego archivado).
