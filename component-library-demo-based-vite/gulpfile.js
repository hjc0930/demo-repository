import gulp from "gulp";
import gulpEsbuild from "gulp-esbuild";
import ts from "gulp-typescript";
import { build } from "vite";
import react from "@vitejs/plugin-react";
import merge2 from "merge2";
import * as rimraf from "rimraf";
import process from "node:process";
import path from "node:path";
import gulpSass from "gulp-sass";
import * as sass from "sass";
import chalk from "chalk";

const buildSass = gulpSass(sass);

const tsDefaultReporter = ts.reporter.defaultReporter();
const cwd = process.cwd();
const libDir = path.resolve(cwd, "lib");
const esDir = path.resolve(cwd, "es");
const distDir = path.resolve(cwd, "dist");

const dist = async (done) => {
  const res = Object.keys(process.env).reduce((acc, key) => {
    if (key.startsWith("VITE_")) {
      acc[key] = process.env[key];
    }
    return acc;
  }, {});
  rimraf.sync(distDir);
  await build({
    plugins: [react()],
    define: {
      "process.env": {
        ...res,
        NODE_ENV: JSON.stringify("production"),
      },
    },

    build: {
      copyPublicDir: false,
      outDir: "dist",
      lib: {
        entry: "./index.js",
        name: "MyComponentLibrary",
        fileName: () => `my-component-library.min.js`,
        cssFileName: `my-component-library.min`,
        formats: ["umd"],
      },
      rollupOptions: {
        output: {
          globals: {
            react: "React",
            "react-dom": "ReactDOM",
          },
        },
      },
    },
  });
  done();
};

/**
 *
 * @param {"esm" | "cjs"} format
 * @returns
 */
const compile = (format = "esm") => {
  const destDir = format === "esm" ? "es" : "lib";
  rimraf.sync(path.resolve(cwd, destDir));

  const source = ["src/components/**/*.ts", "src/components/**/*.tsx"];
  const tsResultStream = gulp.src(source).pipe(
    ts(
      {
        noUnusedParameters: true,
        noUnusedLocals: true,
        strictNullChecks: true,
        target: "es2020",
        jsx: "preserve",
        moduleResolution: "node",
        declaration: true,
        allowSyntheticDefaultImports: true,
      },
      {
        error(e) {
          tsDefaultReporter.error(e, undefined);
        },
        finish: tsDefaultReporter.finish,
      }
    )
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
