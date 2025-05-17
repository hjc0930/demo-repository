import gulp from "gulp";
import gulpEsbuild from "gulp-esbuild";
import ts from "gulp-typescript";
import { build } from "vite";
import merge2 from "merge2";
import * as rimraf from "rimraf";
import process from "node:process";
import path from "node:path";
import gulpSass from "gulp-sass";
import * as sass from "sass";
import chalk from "chalk";
import { getProjectPath } from "./utils/projectHelper";
import getTsConfig from "./getTsConfig";

const buildSass = gulpSass(sass);

const tsConfig = getTsConfig();
const tsDefaultReporter = ts.reporter.defaultReporter();
const cwd = process.cwd();
const libDir = path.resolve(cwd, "lib");
const esDir = path.resolve(cwd, "es");
const distDir = path.resolve(cwd, "dist");

const dist = async (done: () => void) => {
  rimraf.sync(distDir);
  const configModule = await import(getProjectPath("vite.config.js"));
  const viteConfig = configModule.default || configModule;
  await build(viteConfig);
  done();
};

const compile = (format: "esm" | "cjs" = "esm") => {
  const destDir = format === "esm" ? "es" : "lib";
  rimraf.sync(path.resolve(cwd, destDir));

  const source = ["src/components/**/*.ts", "src/components/**/*.tsx"];
  const tsResultStream = gulp.src(source).pipe(
    ts(tsConfig, {
      error: (e) => {
        // @ts-ignore
        tsDefaultReporter?.error?.(e, undefined);
      },
      finish: tsDefaultReporter.finish,
    })
  );

  const tdsStream = tsResultStream.dts;
  const jsStream = tsResultStream.js.pipe(
    gulpEsbuild({
      platform: "browser",
      jsx: "automatic",
      bundle: false,
      minify: false,
      format,
      target: "es2020",
    })
  );
  const assetStream = gulp.src([
    "src/components/**/*.css",
    "src/components/**/*.scss",
  ]);

  const stylesStream = gulp
    .src(["src/components/**/*.scss"])
    .pipe(buildSass().on("error", buildSass.logError));

  return merge2(tdsStream, jsStream, stylesStream, assetStream).pipe(
    gulp.dest(destDir)
  );
};

gulp.task("clean", (done) => {
  console.log(chalk.yellow("[component-tools]"), "Start to clean...");
  rimraf.sync(libDir);
  rimraf.sync(esDir);
  rimraf.sync(distDir);
  done();
});

gulp.task("dist", (done) => {
  console.log(chalk.yellow("[component-tools]"), "Parallel compile to dist...");
  dist(done);
});

gulp.task("compile-with-esm", () => {
  console.log(chalk.yellow("[component-tools]"), "Parallel compile to es...");
  return compile("esm");
});

gulp.task("compile-with-cjs", () => {
  console.log(chalk.yellow("[component-tools]"), "Parallel compile to js...");
  return compile("cjs");
});

gulp.task(
  "compile",
  gulp.series(
    "clean",
    gulp.parallel("dist", "compile-with-esm", "compile-with-cjs")
  )
);
