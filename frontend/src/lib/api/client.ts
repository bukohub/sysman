export class ApiError extends Error {
  constructor(
    public readonly status: number,
    public readonly title: string,
    message: string,
    public readonly properties: Record<string, unknown> = {}
  ) {
    super(message);
  }
}

const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080/api/v1";

async function parsearError(response: Response): Promise<never> {
  let problemDetail: Record<string, unknown> = {};
  try {
    problemDetail = await response.json();
  } catch {
    // respuesta sin cuerpo JSON (ej. error de red ya manejado por el caller)
  }
  throw new ApiError(
    response.status,
    String(problemDetail.title ?? "Error"),
    String(problemDetail.detail ?? "Ocurrio un error en la solicitud"),
    problemDetail
  );
}

export async function apiFetch<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${BASE_URL}${path}`, {
    ...init,
    headers: {
      "Content-Type": "application/json",
      ...(init?.headers ?? {})
    },
    cache: "no-store"
  });

  if (!response.ok) {
    await parsearError(response);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return (await response.json()) as T;
}
