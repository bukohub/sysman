import { Orden } from "@/lib/types/orden";
import { EstadoBadge } from "@/components/orden/EstadoBadge";

interface Props {
  orden: Orden;
}

const ETIQUETAS_TIPO: Record<string, string> = {
  INSTALACION: "Instalacion",
  MANTENIMIENTO: "Mantenimiento",
  CORTE: "Corte",
  RECONEXION: "Reconexion",
  INSPECCION: "Inspeccion",
  REPARACION_EMERGENCIA: "Reparacion de emergencia"
};

export function OrdenDetalle({ orden }: Props) {
  return (
    <div className="tarjeta">
      <div className="encabezado-pagina" style={{ marginBottom: "16px" }}>
        <h2>Orden #{orden.id}</h2>
        <EstadoBadge estado={orden.estado} />
      </div>

      <dl className="detalle-grid">
        <div>
          <dt>Cliente</dt>
          <dd>{orden.cliente.nombre}</dd>
        </div>
        <div>
          <dt>Documento</dt>
          <dd>{orden.cliente.numeroIdentificacion}</dd>
        </div>
        <div>
          <dt>Tipo de orden</dt>
          <dd>{ETIQUETAS_TIPO[orden.tipoOrden] ?? orden.tipoOrden}</dd>
        </div>
        <div>
          <dt>Version</dt>
          <dd>{orden.version}</dd>
        </div>
        <div className="full">
          <dt>Direccion del servicio</dt>
          <dd>{orden.direccionServicio}</dd>
        </div>
        <div className="full">
          <dt>Descripcion</dt>
          <dd>{orden.descripcion}</dd>
        </div>
        <div>
          <dt>Creada</dt>
          <dd>{new Date(orden.fechaCreacion).toLocaleString("es-CO")}</dd>
        </div>
        {orden.fechaModificacion && (
          <div>
            <dt>Ultima modificacion</dt>
            <dd>{new Date(orden.fechaModificacion).toLocaleString("es-CO")}</dd>
          </div>
        )}
      </dl>
    </div>
  );
}
