import process from "node:process";

function checkNodeVersion() {
  // 12.22.12
  const version = process.version.slice(1);
  const [major, minor, patch] = version
    .split(".")
    .map((item) => parseInt(item, 10));
  console.log({ major, minor, patch });
}

checkNodeVersion();
