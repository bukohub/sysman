"use client";

import { FormEvent, useState } from "react";
import { useRouter } from "next/navigation";
import { actualizarEstadoOrden } from "@/lib/api/ordenes";
import { ApiError } from "@/lib/api/client";
import { EstadoOrden, Orden, TRANSICIONES_VALIDAS } from "@/lib/types/orden";
import { UsuarioInput } from "@/components/orden/UsuarioInput";

interface Props {
  orden: Orden;
}

const ETIQUETAS: Record<EstadoOrden, string> = {
  CREADA: "Creada",
  ASIGNADA: "Asignada",
  EN_PROCESO: "En proceso",
  COMPLETADA: "Completada",
  CANCELADA: "Cancelada",
  RECHAZADA: "Rechazada"
};

export function ActualizarEstadoModal({ orden }: Props) {
  const router = useRouter();
  const [abierto, setAbierto] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [enviando, setEnviando] = useState(false);

  const estadosDisponibles = TRANSICIONES_VALIDAS[orden.estado];

  async function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError(null);
    setEnviando(true);

    const form = new FormData(event.currentTarget);
    try {
      await actualizarEstadoOrden(orden.id, {
        estadoNuevo: form.get("estadoNuevo") as EstadoOrden,
        version: orden.version,
        usuario: String(form.get("usuario")),
        motivo: String(form.get("motivo") ?? "")
      });
      setAbierto(false);
      router.refresh();
    } catch (err) {
      if (err instanceof ApiError && err.status === 409) {
        setError("La orden fue modificada por otro usuario. Recargue la pagina e intente de nuevo.");
      } else if (err instanceof ApiError) {
        setError(err.message);
      } else {
        setError("No se pudo actualizar el estado. Intente nuevamente.");
      }
    } finally {
      setEnviando(false);
    }
  }

  if (estadosDisponibles.length === 0) {
    return <p className="ayuda">Esta orden esta en un estado final y no admite mas transiciones.</p>;
  }

  if (!abierto) {
    return (
      <button className="boton" onClick={() => setAbierto(true)}>
        Actualizar estado
      </button>
    );
  }

  return (
    <form onSubmit={onSubmit}>
      <div className="campo">
        <label htmlFor="estadoNuevo">Nuevo estado</label>
        <select id="estadoNuevo" name="estadoNuevo" required>
          {estadosDisponibles.map((estado) => (
            <option key={estado} value={estado}>
              {ETIQUETAS[estado]}
            </option>
          ))}
        </select>
      </div>
      <UsuarioInput name="usuario" label="Usuario" required />
      <div className="campo">
        <label htmlFor="motivo">Motivo</label>
        <textarea id="motivo" name="motivo" maxLength={500} />
      </div>

      {error && <span className="error">{error}</span>}

      <div style={{ display: "flex", gap: "8px" }}>
        <button type="submit" className="boton" disabled={enviando}>
          {enviando ? "Guardando..." : "Confirmar"}
        </button>
        <button type="button" className="boton boton-secundario" onClick={() => setAbierto(false)}>
          Cancelar
        </button>
      </div>
    </form>
  );
}
