import gulp from "gulp";
import babel from "gulp-babel";
import ts from "gulp-typescript";
import { deleteSync } from "del";

gulp.task("clean", async () => {
  return deleteSync(["lib/**", "es/**", "dist/**"]);
});

gulp.task("es", () => {
  const tsProject = ts.createProject("tsconfig.pro.json", {
    module: "ESNext",
  });
  return tsProject.src().pipe(tsProject()).pipe(gulp.dest("es/"));
});

gulp.task("cjs", () => {
  return gulp
    .src(["./es/**/*.js"])
    .pipe(
      babel({
        configFile: "./.babelrc",
      })
    )
    .pipe(gulp.dest("lib/"));
});

gulp.task("declaration", () => {
  const tsProject = ts.createProject("tsconfig.pro.json", {
    declaration: true,
    emitDeclarationOnly: true,
  });
  return tsProject
    .src()
    .pipe(tsProject())
    .pipe(gulp.dest("es/"))
    .pipe(gulp.dest("lib/"));
});

const tasks = gulp.series("clean", "es", "cjs", "declaration");

export default tasks;
