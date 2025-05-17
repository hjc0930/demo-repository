import gulp from "gulp";
import * as rimraf from "rimraf";
import gulpEsbuild from "gulp-esbuild";
import merge2 from "merge2";
import ts from "gulp-typescript";

const tsDefaultReporter = ts.reporter.defaultReporter();

gulp.task("build", (done) => {
  console.log("Building the project...");
  rimraf.sync("lib");

  const tsResultStream = gulp.src(["src/**/*.ts"]).pipe(
    ts(
      {
        noUnusedParameters: true,
        noUnusedLocals: true,
        strictNullChecks: true,
        target: "ESNext",
        module: "ESNext", // 确保与 ESM 规范兼容
        moduleResolution: "bundler",
        declaration: true,
        allowSyntheticDefaultImports: true,
      },
      {
        error: (e) => {
          // @ts-ignore
          tsDefaultReporter?.error?.(e, undefined);
        },
        finish: tsDefaultReporter.finish,
      }
    )
  );
  const tdsStream = tsResultStream.dts;
  const jsStream = tsResultStream.js.pipe(
    gulpEsbuild({
      platform: "node",
      bundle: false,
      minify: false,
      format: "cjs",
      target: "es2020",
    })
  );
  merge2(tdsStream, jsStream).pipe(gulp.dest("lib"));
  done();
});
