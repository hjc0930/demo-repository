declare namespace Build {
  interface Message {
    errors: string[];
    warnings: string[];
  }
}

declare namespace UtilsType {
  interface Message {
    name: string;
    path: string;
    originSize: number;
    size: string;
    isSizeWarning: boolean;
    joinPathLength: number;
    sizeLength: number;
  }
}
