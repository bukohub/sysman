CREATE OR REPLACE PACKAGE orden_pkg AS

    -- Excepciones personalizadas (codigos de error de aplicacion Oracle: rango -20000 a -20999)
    e_transicion_invalida  EXCEPTION;
    PRAGMA EXCEPTION_INIT(e_transicion_invalida, -20001);

    e_orden_no_encontrada   EXCEPTION;
    PRAGMA EXCEPTION_INIT(e_orden_no_encontrada, -20002);

    e_version_obsoleta      EXCEPTION;
    PRAGMA EXCEPTION_INIT(e_version_obsoleta, -20003);

    PROCEDURE actualizar_estado_orden (
        p_orden_id           IN  orden.id%TYPE,
        p_estado_nuevo       IN  orden.estado%TYPE,
        p_version_esperada   IN  orden.version%TYPE,
        p_usuario            IN  VARCHAR2,
        p_motivo             IN  VARCHAR2,
        p_estado_resultante  OUT orden.estado%TYPE,
        p_version_resultante OUT orden.version%TYPE
    );

END orden_pkg;
/

CREATE OR REPLACE PACKAGE BODY orden_pkg AS

    PROCEDURE actualizar_estado_orden (
        p_orden_id           IN  orden.id%TYPE,
        p_estado_nuevo       IN  orden.estado%TYPE,
        p_version_esperada   IN  orden.version%TYPE,
        p_usuario            IN  VARCHAR2,
        p_motivo             IN  VARCHAR2,
        p_estado_resultante  OUT orden.estado%TYPE,
        p_version_resultante OUT orden.version%TYPE
    ) IS
        v_estado_actual     orden.estado%TYPE;
        v_version_actual    orden.version%TYPE;
        v_existe_transicion NUMBER;
    BEGIN
        -- 1. Lock pesimista puntual: solo durante esta transaccion corta. Si otra
        --    transaccion ya tiene la fila bloqueada, falla rapido (NOWAIT) en vez de
        --    esperar indefinidamente y encadenar bloqueos bajo alto volumen.
        BEGIN
            SELECT estado, version
              INTO v_estado_actual, v_version_actual
              FROM orden
             WHERE id = p_orden_id
               FOR UPDATE NOWAIT;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(-20002, 'Orden no encontrada: ' || p_orden_id);
        END;

        -- 2. Verificar version (optimistic check explicito dentro del lock pesimista)
        IF v_version_actual != p_version_esperada THEN
            RAISE_APPLICATION_ERROR(-20003,
                'Version obsoleta para orden ' || p_orden_id ||
                ': esperada=' || p_version_esperada || ' actual=' || v_version_actual);
        END IF;

        -- 3. Validar transicion contra la tabla de transiciones validas
        SELECT COUNT(*)
          INTO v_existe_transicion
          FROM orden_transicion_valida
         WHERE estado_origen = v_estado_actual
           AND estado_destino = p_estado_nuevo;

        IF v_existe_transicion = 0 THEN
            RAISE_APPLICATION_ERROR(-20001,
                'Transicion invalida de ' || v_estado_actual || ' a ' || p_estado_nuevo);
        END IF;

        -- 4. Insertar registro de historico (auditoria de negocio)
        INSERT INTO orden_historico (orden_id, estado_anterior, estado_nuevo, usuario, motivo, fecha_cambio)
        VALUES (p_orden_id, v_estado_actual, p_estado_nuevo, p_usuario, p_motivo, SYSTIMESTAMP);

        -- 5. Actualizar la orden: nuevo estado + version incrementada + auditoria
        UPDATE orden
           SET estado = p_estado_nuevo,
               version = version + 1,
               modificado_por = p_usuario,
               fecha_modificacion = SYSTIMESTAMP
         WHERE id = p_orden_id;

        p_estado_resultante  := p_estado_nuevo;
        p_version_resultante := v_version_actual + 1;

        -- NOTA: este procedimiento NO hace COMMIT. El commit/rollback es responsabilidad
        -- del llamador (la transaccion @Transactional del backend), de forma que la
        -- llamada a este procedimiento participa en la misma transaccion JDBC que
        -- cualquier otra operacion que el caso de uso necesite realizar.
    END actualizar_estado_orden;

END orden_pkg;
/
