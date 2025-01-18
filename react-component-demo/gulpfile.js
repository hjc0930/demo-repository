import gulp from "gulp";
import del from "del";

gulp.task("clean", () => {
  return del(["dist", "es", "cjs"]);
});

const tasks = gulp.task("build", gulp.series("clean"));

export default tasks;
