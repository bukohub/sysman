import { NextRequest, NextResponse } from "next/server";
import { apiFetch } from "@/lib/api/client";

export async function GET(request: NextRequest) {
  const q = request.nextUrl.searchParams.get("q") ?? "";
  const limit = request.nextUrl.searchParams.get("limit") ?? "10";
  const usuarios = await apiFetch<string[]>(
    `/usuario/sugerencias?q=${encodeURIComponent(q)}&limit=${encodeURIComponent(limit)}`
  );
  return NextResponse.json(usuarios);
}
