"use client";

import { useEffect, useRef, useState } from "react";

interface Props {
  name: string;
  label: string;
  required?: boolean;
  defaultValue?: string;
}

export function UsuarioInput({ name, label, required, defaultValue }: Props) {
  const [texto, setTexto] = useState(defaultValue ?? "");
  const [sugerencias, setSugerencias] = useState<string[]>([]);
  const [abierto, setAbierto] = useState(false);
  const contenedorRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const controlador = new AbortController();
    const temporizador = setTimeout(async () => {
      try {
        const respuesta = await fetch(
          `/api/usuario?q=${encodeURIComponent(texto)}&limit=6`,
          { signal: controlador.signal }
        );
        if (respuesta.ok) {
          setSugerencias(await respuesta.json());
        }
      } catch {
        // peticion cancelada por una nueva busqueda; se ignora
      }
    }, 200);

    return () => {
      clearTimeout(temporizador);
      controlador.abort();
    };
  }, [texto]);

  useEffect(() => {
    function onClickFuera(event: MouseEvent) {
      if (contenedorRef.current && !contenedorRef.current.contains(event.target as Node)) {
        setAbierto(false);
      }
    }
    document.addEventListener("mousedown", onClickFuera);
    return () => document.removeEventListener("mousedown", onClickFuera);
  }, []);

  const sugerenciasFiltradas = sugerencias.filter(
    (sugerencia) => sugerencia.toUpperCase() !== texto.trim().toUpperCase()
  );

  return (
    <div className="campo combobox" ref={contenedorRef}>
      <label htmlFor={`${name}-input`}>{label}</label>
      <input
        id={`${name}-input`}
        name={name}
        type="text"
        required={required}
        maxLength={100}
        autoComplete="off"
        value={texto}
        onChange={(event) => {
          setTexto(event.target.value);
          setAbierto(true);
        }}
        onFocus={() => setAbierto(true)}
      />

      {abierto && sugerenciasFiltradas.length > 0 && (
        <div className="combobox-lista">
          {sugerenciasFiltradas.map((sugerencia) => (
            <div
              key={sugerencia}
              className="combobox-opcion"
              onClick={() => {
                setTexto(sugerencia);
                setAbierto(false);
              }}
            >
              <span>{sugerencia}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
