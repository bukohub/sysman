import Link from "next/link";

export function AppHeader() {
  return (
    <header className="app-header">
      <div className="app-header__inner">
        <Link href="/orden" className="app-header__brand">
          <span className="app-header__brand-mark">SM</span>
          Sysman &middot; Ordenes operativas
        </Link>
      </div>
    </header>
  );
}
