import Link from "next/link";
import { consultarOrden } from "@/lib/api/ordenes";
import { OrdenDetalle } from "@/components/orden/OrdenDetalle";
import { OrdenHistorialTimeline } from "@/components/orden/OrdenHistorialTimeline";
import { ActualizarEstadoModal } from "@/components/orden/ActualizarEstadoModal";

export default async function DetalleOrdenPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = await params;
  const orden = await consultarOrden(Number(id));

  return (
    <div className="contenedor">
      <Link href="/orden" className="volver">
        &larr; Volver al listado
      </Link>
      <OrdenDetalle orden={orden} />

      <div className="tarjeta">
        <h3 className="tarjeta-titulo">Actualizar estado</h3>
        <ActualizarEstadoModal orden={orden} />
      </div>

      <div className="tarjeta">
        <h3 className="tarjeta-titulo">Historial</h3>
        <OrdenHistorialTimeline historico={orden.historico} />
      </div>
    </div>
  );
}
