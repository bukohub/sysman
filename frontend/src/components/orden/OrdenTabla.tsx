import Link from "next/link";
import { OrdenResumen } from "@/lib/types/orden";
import { EstadoBadge } from "@/components/orden/EstadoBadge";

interface Props {
  ordenes: OrdenResumen[];
}

const ETIQUETAS_TIPO: Record<string, string> = {
  INSTALACION: "Instalacion",
  MANTENIMIENTO: "Mantenimiento",
  CORTE: "Corte",
  RECONEXION: "Reconexion",
  INSPECCION: "Inspeccion",
  REPARACION_EMERGENCIA: "Reparacion de emergencia"
};

export function OrdenTabla({ ordenes }: Props) {
  if (ordenes.length === 0) {
    return <p className="estado-vacio">No hay ordenes que coincidan con los filtros.</p>;
  }

  return (
    <div className="tabla-envoltorio">
      <table>
        <thead>
          <tr>
            <th>Orden</th>
            <th>Cliente</th>
            <th>Tipo</th>
            <th>Estado</th>
            <th>Fecha creacion</th>
          </tr>
        </thead>
        <tbody>
          {ordenes.map((orden) => (
            <tr key={orden.id}>
              <td>
                <Link href={`/orden/${orden.id}`} className="id-orden">
                  #{orden.id}
                </Link>
              </td>
              <td>{orden.nombreCliente}</td>
              <td>{ETIQUETAS_TIPO[orden.tipoOrden] ?? orden.tipoOrden}</td>
              <td>
                <EstadoBadge estado={orden.estado} />
              </td>
              <td>{new Date(orden.fechaCreacion).toLocaleString("es-CO")}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
