const server = Bun.serve({
  port: 3000,
  async fetch(req) {
    console.log(req);

    return new Response(
      JSON.stringify({
        a: 1,
        b: 2,
      })
    );
  },
});

console.log(`Listening on http://localhost:${server.port} ...`);
