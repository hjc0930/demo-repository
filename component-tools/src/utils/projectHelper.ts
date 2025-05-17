import process from "node:process";
import path from "node:path";

const cwd: string = process.cwd();

export function getProjectPath(...filePath: string[]): string {
  return path.join(cwd, ...filePath);
}

export function resolve(moduleName: string): string {
  return require.resolve(moduleName);
}
