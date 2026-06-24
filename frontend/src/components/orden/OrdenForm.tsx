"use client";

import { FormEvent, useState } from "react";
import { useRouter } from "next/navigation";
import { crearOrden } from "@/lib/api/ordenes";
import { ApiError } from "@/lib/api/client";
import { TipoOrden } from "@/lib/types/orden";
import { ClienteCombobox } from "@/components/orden/ClienteCombobox";
import { UsuarioInput } from "@/components/orden/UsuarioInput";

const TIPOS: TipoOrden[] = ["INSTALACION", "MANTENIMIENTO", "CORTE", "RECONEXION", "INSPECCION", "REPARACION_EMERGENCIA"];

const ETIQUETAS_TIPO: Record<TipoOrden, string> = {
  INSTALACION: "Instalacion",
  MANTENIMIENTO: "Mantenimiento",
  CORTE: "Corte",
  RECONEXION: "Reconexion",
  INSPECCION: "Inspeccion",
  REPARACION_EMERGENCIA: "Reparacion de emergencia"
};

export function OrdenForm() {
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);
  const [enviando, setEnviando] = useState(false);

  async function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError(null);

    const form = new FormData(event.currentTarget);
    const clienteId = Number(form.get("clienteId"));
    if (!clienteId) {
      setError("Debe seleccionar un cliente de la lista de busqueda.");
      return;
    }

    setEnviando(true);
    try {
      const orden = await crearOrden({
        clienteId,
        tipoOrden: form.get("tipoOrden") as TipoOrden,
        descripcion: String(form.get("descripcion")),
        direccionServicio: String(form.get("direccionServicio")),
        usuario: String(form.get("usuario"))
      });
      router.push(`/orden/${orden.id}`);
    } catch (err) {
      if (err instanceof ApiError) {
        setError(err.message);
      } else {
        setError("No se pudo crear la orden. Intente nuevamente.");
      }
    } finally {
      setEnviando(false);
    }
  }

  return (
    <form onSubmit={onSubmit} className="tarjeta">
      <ClienteCombobox name="clienteId" label="Cliente" required />

      <div className="campo">
        <label htmlFor="tipoOrden">Tipo de orden</label>
        <select id="tipoOrden" name="tipoOrden" required>
          {TIPOS.map((tipo) => (
            <option key={tipo} value={tipo}>
              {ETIQUETAS_TIPO[tipo]}
            </option>
          ))}
        </select>
      </div>
      <div className="campo">
        <label htmlFor="descripcion">Descripcion</label>
        <textarea id="descripcion" name="descripcion" required maxLength={1000} />
      </div>
      <div className="campo">
        <label htmlFor="direccionServicio">Direccion del servicio</label>
        <input id="direccionServicio" name="direccionServicio" type="text" required maxLength={300} />
      </div>
      <UsuarioInput name="usuario" label="Usuario que registra" required />

      {error && <span className="error">{error}</span>}

      <button type="submit" className="boton" disabled={enviando}>
        {enviando ? "Creando..." : "Crear orden"}
      </button>
    </form>
  );
}
