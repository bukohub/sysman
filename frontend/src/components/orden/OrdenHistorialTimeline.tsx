import { OrdenHistorico } from "@/lib/types/orden";

interface Props {
  historico: OrdenHistorico[];
}

const ETIQUETAS: Record<string, string> = {
  CREADA: "Creada",
  ASIGNADA: "Asignada",
  EN_PROCESO: "En proceso",
  COMPLETADA: "Completada",
  CANCELADA: "Cancelada",
  RECHAZADA: "Rechazada"
};

export function OrdenHistorialTimeline({ historico }: Props) {
  if (historico.length === 0) {
    return <p className="estado-vacio">Sin historial.</p>;
  }

  return (
    <ul className="timeline">
      {historico.map((item) => (
        <li key={item.id} className="timeline-item">
          <div className="timeline-item__titulo">
            {item.estadoAnterior
              ? `${ETIQUETAS[item.estadoAnterior] ?? item.estadoAnterior} -> ${ETIQUETAS[item.estadoNuevo] ?? item.estadoNuevo}`
              : ETIQUETAS[item.estadoNuevo] ?? item.estadoNuevo}
          </div>
          <div className="timeline-item__meta">
            {new Date(item.fechaCambio).toLocaleString("es-CO")} &middot; {item.usuario}
          </div>
          {item.motivo && <div className="timeline-item__motivo">{item.motivo}</div>}
        </li>
      ))}
    </ul>
  );
}
