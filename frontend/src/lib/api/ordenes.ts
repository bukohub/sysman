import { apiFetch } from "./client";
import {
  ActualizarEstadoOrdenRequest,
  CrearOrdenRequest,
  EstadoOrden,
  Orden,
  OrdenResumen,
  PageResponse
} from "@/lib/types/orden";

export interface FiltroListadoOrdenes {
  estado?: EstadoOrden;
  fechaInicio?: string;
  fechaFin?: string;
  page?: number;
  size?: number;
}

export async function listarOrdenes(filtro: FiltroListadoOrdenes): Promise<PageResponse<OrdenResumen>> {
  const params = new URLSearchParams();
  if (filtro.estado) params.set("estado", filtro.estado);
  if (filtro.fechaInicio) params.set("fechaInicio", filtro.fechaInicio);
  if (filtro.fechaFin) params.set("fechaFin", filtro.fechaFin);
  params.set("page", String(filtro.page ?? 0));
  params.set("size", String(filtro.size ?? 20));

  return apiFetch<PageResponse<OrdenResumen>>(`/orden?${params.toString()}`);
}

export async function consultarOrden(id: number): Promise<Orden> {
  return apiFetch<Orden>(`/orden/${id}`);
}

export async function crearOrden(request: CrearOrdenRequest): Promise<Orden> {
  return apiFetch<Orden>(`/orden`, {
    method: "POST",
    body: JSON.stringify(request)
  });
}

export async function actualizarEstadoOrden(id: number, request: ActualizarEstadoOrdenRequest): Promise<Orden> {
  return apiFetch<Orden>(`/orden/${id}/estado`, {
    method: "PUT",
    body: JSON.stringify(request)
  });
}
