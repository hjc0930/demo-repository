import { createInterface } from "node:readline";
import process from "node:process";

const rl = createInterface({
  input: process.stdin,
  output: process.stdout,
});
rl.on("line", (line) => {
  console.log(line);
  // switch (line.trim()) {
  //   case "hello":
  //     console.log("world!");
  //     break;
  //   default:
  //     console.log(`你输入的是：'${line.trim()}'`);
  //     break;
  // }
});
