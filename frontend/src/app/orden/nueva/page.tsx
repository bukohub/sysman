import Link from "next/link";
import { OrdenForm } from "@/components/orden/OrdenForm";

export default function NuevaOrdenPage() {
  return (
    <div className="contenedor" style={{ maxWidth: "640px" }}>
      <Link href="/orden" className="volver">
        &larr; Volver al listado
      </Link>
      <div className="encabezado-pagina">
        <div>
          <h1>Nueva orden</h1>
          <p>Registre una nueva orden operativa para un cliente existente.</p>
        </div>
      </div>
      <OrdenForm />
    </div>
  );
}
