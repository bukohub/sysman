"use client";

import { useRouter } from "next/navigation";
import { FormEvent } from "react";
import { EstadoOrden } from "@/lib/types/orden";

const ESTADOS: EstadoOrden[] = ["CREADA", "ASIGNADA", "EN_PROCESO", "COMPLETADA", "CANCELADA", "RECHAZADA"];

const ETIQUETAS: Record<EstadoOrden, string> = {
  CREADA: "Creada",
  ASIGNADA: "Asignada",
  EN_PROCESO: "En proceso",
  COMPLETADA: "Completada",
  CANCELADA: "Cancelada",
  RECHAZADA: "Rechazada"
};

interface Props {
  estadoActual?: string;
  fechaInicioActual?: string;
  fechaFinActual?: string;
}

export function OrdenFiltros({ estadoActual, fechaInicioActual, fechaFinActual }: Props) {
  const router = useRouter();

  function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const form = new FormData(event.currentTarget);
    const params = new URLSearchParams();
    const estado = form.get("estado") as string;
    const fechaInicio = form.get("fechaInicio") as string;
    const fechaFin = form.get("fechaFin") as string;
    if (estado) params.set("estado", estado);
    if (fechaInicio) params.set("fechaInicio", `${fechaInicio}T00:00:00`);
    if (fechaFin) params.set("fechaFin", `${fechaFin}T23:59:59`);
    router.push(`/orden?${params.toString()}`);
  }

  return (
    <form onSubmit={onSubmit} className="tarjeta fila-formulario">
      <div className="campo">
        <label htmlFor="estado">Estado</label>
        <select id="estado" name="estado" defaultValue={estadoActual ?? ""}>
          <option value="">Todos</option>
          {ESTADOS.map((estado) => (
            <option key={estado} value={estado}>
              {ETIQUETAS[estado]}
            </option>
          ))}
        </select>
      </div>
      <div className="campo">
        <label htmlFor="fechaInicio">Desde</label>
        <input id="fechaInicio" name="fechaInicio" type="date" defaultValue={fechaInicioActual?.slice(0, 10) ?? ""} />
      </div>
      <div className="campo">
        <label htmlFor="fechaFin">Hasta</label>
        <input id="fechaFin" name="fechaFin" type="date" defaultValue={fechaFinActual?.slice(0, 10) ?? ""} />
      </div>
      <div className="campo" style={{ flex: "0 0 auto" }}>
        <label>&nbsp;</label>
        <button type="submit" className="boton">
          Filtrar
        </button>
      </div>
    </form>
  );
}
