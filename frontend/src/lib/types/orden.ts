export type EstadoOrden =
  | "CREADA"
  | "ASIGNADA"
  | "EN_PROCESO"
  | "COMPLETADA"
  | "CANCELADA"
  | "RECHAZADA";

export type TipoOrden =
  | "INSTALACION"
  | "MANTENIMIENTO"
  | "CORTE"
  | "RECONEXION"
  | "INSPECCION"
  | "REPARACION_EMERGENCIA";

export interface ClienteResumen {
  id: number;
  numeroIdentificacion: string;
  nombre: string;
}

export interface OrdenHistorico {
  id: number;
  estadoAnterior: EstadoOrden | null;
  estadoNuevo: EstadoOrden;
  fechaCambio: string;
  usuario: string;
  motivo: string | null;
}

export interface Orden {
  id: number;
  cliente: ClienteResumen;
  tipoOrden: TipoOrden;
  estado: EstadoOrden;
  descripcion: string;
  direccionServicio: string;
  fechaCreacion: string;
  fechaModificacion: string | null;
  version: number;
  historico: OrdenHistorico[];
}

export interface OrdenResumen {
  id: number;
  clienteId: number;
  nombreCliente: string;
  tipoOrden: TipoOrden;
  estado: EstadoOrden;
  fechaCreacion: string;
}

export interface PageResponse<T> {
  contenido: T[];
  pagina: number;
  tamanioPagina: number;
  totalElementos: number;
  totalPaginas: number;
}

export interface CrearOrdenRequest {
  clienteId: number;
  tipoOrden: TipoOrden;
  descripcion: string;
  direccionServicio: string;
  usuario: string;
}

export interface ActualizarEstadoOrdenRequest {
  estadoNuevo: EstadoOrden;
  version: number;
  usuario: string;
  motivo?: string;
}

export const TRANSICIONES_VALIDAS: Record<EstadoOrden, EstadoOrden[]> = {
  CREADA: ["ASIGNADA", "CANCELADA", "RECHAZADA"],
  ASIGNADA: ["EN_PROCESO", "CANCELADA"],
  EN_PROCESO: ["COMPLETADA", "CANCELADA"],
  COMPLETADA: [],
  CANCELADA: [],
  RECHAZADA: ["CREADA"]
};
