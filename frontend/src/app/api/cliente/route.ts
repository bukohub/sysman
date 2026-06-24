import { NextRequest, NextResponse } from "next/server";
import { apiFetch } from "@/lib/api/client";
import { ClienteResumen } from "@/lib/types/orden";

export async function GET(request: NextRequest) {
  const q = request.nextUrl.searchParams.get("q") ?? "";
  const limit = request.nextUrl.searchParams.get("limit") ?? "10";
  const clientes = await apiFetch<ClienteResumen[]>(
    `/cliente?q=${encodeURIComponent(q)}&limit=${encodeURIComponent(limit)}`
  );
  return NextResponse.json(clientes);
}
