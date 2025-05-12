#! /usr/bin/env node
import minimist from "minimist";
import chalk from "chalk";
import gulp from "gulp";
import process from "node:process";
import "../gulpfile";

const argv = minimist(process.argv.slice(2));

console.log(
  chalk.yellow("[component-tools]"),
  chalk.green("Execute:"),
  argv._[0]
);

/**
 *
 * @param {String} toRun
 */
const runTask = (toRun) => {
  const metadata = { task: toRun };
  const taskInstance = gulp.task(toRun);
  if (taskInstance === undefined) {
    gulp.emit("task_not_found", metadata);
    return;
  }
  const start = process.hrtime();
  gulp.emit("task_start", metadata);
  try {
    taskInstance.apply(gulp);
    metadata.hrDuration = process.hrtime(start);
    gulp.emit("task_stop", metadata);
    gulp.emit("stop");
  } catch (error) {
    err.hrDuration = process.hrtime(start);
    err.task = metadata.task;
    gulp.emit("task_err", err);
  }
};

runTask(argv._[0]);
