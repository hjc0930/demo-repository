var net = require("net");
var server = net.createServer();

server.once("error", function (err) {
  if (err.code === "EADDRINUSE") {
    // port is currently in use
    console.log("port is currently in use");
  }
});

server.once("listening", function () {
  // close the server if listening doesn't fail
  console.log("Port is currently available");
  server.close();
});

server.listen(3000, "0.0.0.0");
