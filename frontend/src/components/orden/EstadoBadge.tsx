import { EstadoOrden } from "@/lib/types/orden";

const ETIQUETAS: Record<EstadoOrden, string> = {
  CREADA: "Creada",
  ASIGNADA: "Asignada",
  EN_PROCESO: "En proceso",
  COMPLETADA: "Completada",
  CANCELADA: "Cancelada",
  RECHAZADA: "Rechazada"
};

export function EstadoBadge({ estado }: { estado: EstadoOrden }) {
  return <span className={`badge badge-${estado}`}>{ETIQUETAS[estado]}</span>;
}
