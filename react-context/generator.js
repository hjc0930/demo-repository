import a from "./.configrc.js";
import { writeFileSync } from "node:fs";

let str = "";

Object.keys(a).forEach((item) => {
  str += `declare module ${item} {
    const component: React.ReactElement
    export default component;
}
\n`;
});

console.log(str);
