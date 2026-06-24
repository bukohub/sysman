import Link from "next/link";
import { listarOrdenes } from "@/lib/api/ordenes";
import { OrdenFiltros } from "@/components/orden/OrdenFiltros";
import { OrdenTabla } from "@/components/orden/OrdenTabla";
import { EstadoOrden } from "@/lib/types/orden";

interface SearchParams {
  estado?: string;
  fechaInicio?: string;
  fechaFin?: string;
  page?: string;
}

export default async function ListadoOrdenesPage({
  searchParams: searchParamsPromise
}: {
  searchParams: Promise<SearchParams>;
}) {
  const searchParams = await searchParamsPromise;
  const page = Number(searchParams.page ?? "0");

  const resultado = await listarOrdenes({
    estado: searchParams.estado as EstadoOrden | undefined,
    fechaInicio: searchParams.fechaInicio,
    fechaFin: searchParams.fechaFin,
    page
  });

  return (
    <div className="contenedor">
      <div className="encabezado-pagina">
        <div>
          <h1>Ordenes operativas</h1>
          <p>Gestione y consulte el ciclo de vida de las ordenes de servicio.</p>
        </div>
        <Link href="/orden/nueva" className="boton">
          + Nueva orden
        </Link>
      </div>

      <OrdenFiltros
        estadoActual={searchParams.estado}
        fechaInicioActual={searchParams.fechaInicio}
        fechaFinActual={searchParams.fechaFin}
      />

      <div className="tarjeta" style={{ padding: 0 }}>
        <OrdenTabla ordenes={resultado.contenido} />
      </div>

      <div className="paginacion">
        {resultado.pagina > 0 && (
          <Link href={`/orden?${buildQuery(searchParams, resultado.pagina - 1)}`}>&larr; Anterior</Link>
        )}
        <span>
          Pagina {resultado.pagina + 1} de {Math.max(resultado.totalPaginas, 1)} ({resultado.totalElementos} ordenes)
        </span>
        {resultado.pagina + 1 < resultado.totalPaginas && (
          <Link href={`/orden?${buildQuery(searchParams, resultado.pagina + 1)}`}>Siguiente &rarr;</Link>
        )}
      </div>
    </div>
  );
}

function buildQuery(searchParams: SearchParams, page: number): string {
  const params = new URLSearchParams();
  if (searchParams.estado) params.set("estado", searchParams.estado);
  if (searchParams.fechaInicio) params.set("fechaInicio", searchParams.fechaInicio);
  if (searchParams.fechaFin) params.set("fechaFin", searchParams.fechaFin);
  params.set("page", String(page));
  return params.toString();
}
