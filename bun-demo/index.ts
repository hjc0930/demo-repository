const server = Bun.serve({
  port: 3000,
  async fetch(req) {
    const chunks = [];

    return req.body
      ?.getReader()
      .read()
      .then(function process({ done, value }) {
        if (done) {
          // 将所有数据块连接起来，解码为字符串，然后解析为 JavaScript 对象
          const text = decoder.decode(new Uint8Array(chunks.flat()));
          const data = JSON.parse(text);

          console.log("Received JSON data", data);

          return;
        }

        // 存储数据块
        chunks.push(value);

        return reader.read().then(process);
      });

    return new Response(
      JSON.stringify({
        a: 1,
        b: 2,
      })
    );
  },
});

console.log(`Listening on http://localhost:${server.port} ...`);
