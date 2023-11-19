function classesJoin(this: any, ...args: any[]): string {
  let i = 0;
  const classes: string[] = [];

  while (i < args.length) {
    const arg = args[i++];
    if (!arg) continue;

    if (typeof arg === "string" || typeof arg === "number") {
      classes.push((this && this[arg]) || arg);
    } else if (Array.isArray(arg) && arg.length) {
      const inner = classesJoin.apply(this, arg);
      if (inner) {
        classes.push(inner);
      }
    } else if (
      Object.prototype.toString.call(arg).toLocaleLowerCase() ===
      "[object object]"
    ) {
      if (
        arg.toString !== Object.prototype.toString &&
        !arg.toString.toString().includes("[native code]")
      ) {
        classes.push(arg.toString());
        continue;
      }

      for (const key in arg) {
        if (Reflect.has(arg, key) && arg[key]) {
          classes.push((this && this[key]) || key);
        }
      }
    }
  }

  return classes.join(" ");
}

export default classesJoin;
