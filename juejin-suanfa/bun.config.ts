await Bun.build({
  target: "bun",
  entrypoints: ["./index.ts"],
  outdir: "./build",
});
