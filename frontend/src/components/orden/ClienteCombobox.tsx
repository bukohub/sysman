"use client";

import { useEffect, useRef, useState } from "react";
import { ClienteResumen } from "@/lib/types/orden";

interface Props {
  name: string;
  label: string;
  required?: boolean;
}

export function ClienteCombobox({ name, label, required }: Props) {
  const [texto, setTexto] = useState("");
  const [resultados, setResultados] = useState<ClienteResumen[]>([]);
  const [abierto, setAbierto] = useState(false);
  const [buscando, setBuscando] = useState(false);
  const [seleccionado, setSeleccionado] = useState<ClienteResumen | null>(null);
  const contenedorRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (seleccionado || texto.trim().length === 0) {
      setResultados([]);
      return;
    }

    const controlador = new AbortController();
    const temporizador = setTimeout(async () => {
      setBuscando(true);
      try {
        const respuesta = await fetch(
          `/api/cliente?q=${encodeURIComponent(texto)}&limit=8`,
          { signal: controlador.signal }
        );
        if (respuesta.ok) {
          setResultados(await respuesta.json());
        }
      } catch {
        // peticion cancelada por una nueva busqueda; se ignora
      } finally {
        setBuscando(false);
      }
    }, 250);

    return () => {
      clearTimeout(temporizador);
      controlador.abort();
    };
  }, [texto, seleccionado]);

  useEffect(() => {
    function onClickFuera(event: MouseEvent) {
      if (contenedorRef.current && !contenedorRef.current.contains(event.target as Node)) {
        setAbierto(false);
      }
    }
    document.addEventListener("mousedown", onClickFuera);
    return () => document.removeEventListener("mousedown", onClickFuera);
  }, []);

  function seleccionar(cliente: ClienteResumen) {
    setSeleccionado(cliente);
    setTexto("");
    setAbierto(false);
  }

  function limpiar() {
    setSeleccionado(null);
    setTexto("");
  }

  return (
    <div className="campo combobox" ref={contenedorRef}>
      <label htmlFor={`${name}-busqueda`}>{label}</label>
      <input type="hidden" name={name} value={seleccionado?.id ?? ""} required={required} />

      {seleccionado ? (
        <div className="combobox-seleccion">
          <div>
            <strong>{seleccionado.nombre}</strong>
            <small>Documento: {seleccionado.numeroIdentificacion}</small>
          </div>
          <button type="button" className="boton boton-secundario" onClick={limpiar}>
            Cambiar
          </button>
        </div>
      ) : (
        <input
          id={`${name}-busqueda`}
          type="text"
          placeholder="Buscar por nombre o numero de identificacion..."
          value={texto}
          autoComplete="off"
          onChange={(event) => {
            setTexto(event.target.value);
            setAbierto(true);
          }}
          onFocus={() => setAbierto(true)}
        />
      )}

      {abierto && !seleccionado && texto.trim().length > 0 && (
        <div className="combobox-lista">
          {buscando && <div className="combobox-vacio">Buscando...</div>}
          {!buscando && resultados.length === 0 && (
            <div className="combobox-vacio">Sin coincidencias para &quot;{texto}&quot;</div>
          )}
          {!buscando &&
            resultados.map((cliente) => (
              <div key={cliente.id} className="combobox-opcion" onClick={() => seleccionar(cliente)}>
                <span>{cliente.nombre}</span>
                <small>Documento: {cliente.numeroIdentificacion}</small>
              </div>
            ))}
        </div>
      )}
    </div>
  );
}
