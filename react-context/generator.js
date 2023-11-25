import a from "./.configrc.js";
import { writeFileSync } from "node:fs";

let str = "";

Object.keys(a).forEach((item) => {
  str += `declare module ${item} {
  interface A {}
}
\n`;
});

console.log(str);
