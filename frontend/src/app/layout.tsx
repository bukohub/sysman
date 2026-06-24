import type { Metadata } from "next";
import "./globals.css";
import { AppHeader } from "@/components/layout/AppHeader";

export const metadata: Metadata = {
  title: "Sysman - Ordenes Operativas",
  description: "Gestion de ordenes operativas de servicios publicos"
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="es">
      <body>
        <AppHeader />
        {children}
      </body>
    </html>
  );
}
