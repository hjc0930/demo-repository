import ts from "typescript";
import fs from "node:fs";

const readTsFile = () => {
  const content = fs.readFileSync("./app.config.ts", "utf8"); // 读取文件内容

  const compiled = ts.transpileModule(content, {
    // 编译 TypeScript 代码
    compilerOptions: {
      target: ts.ScriptTarget.ESNext,
      module: ts.ModuleKind.ESNext,
    },
  });

  const result =
    "data:text/javascript;base64," + encodeURIComponent(compiled.outputText);
  import(result);
};

readTsFile();
