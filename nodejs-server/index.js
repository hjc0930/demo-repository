import { createServer } from "node:http";

const server = createServer((req, res) => {
  res.writeHead(200, { "Content-Type": "text/plain" });
  res.end("Hello, world!");
});

server.listen(3000, () => {
  console.log("Listening to :3000");
});
