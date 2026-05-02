/// <reference types="vite/client" />
interface ViteTypeOptions {
  strictImportMetaEnv: unknown;
}

interface ImportMetaEnv {
  readonly VITE_APP_TITLE: string;
  // more env variable...
  readonly VITE_BASE_URL: string;
  readonly VITE_ENABLE_MOCK: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
