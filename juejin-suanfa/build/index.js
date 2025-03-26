// @bun
// index.ts
var {serve } = globalThis.Bun;
var result = serve({
  async fetch(req) {
    const path = new URL(req.url).pathname;
    const method = req.method;
    if (path === "/")
      return new Response("Welcome to Bun!");
    if (path === "/abc")
      return Response.redirect("/source", 301);
    if (path === "/source")
      return new Response(Bun.file(import.meta.path));
    if (path === "/api")
      return Response.json({ some: "buns", for: "you" });
    if (req.method === "POST" && path === "/api/post") {
      const data = await req.json();
      console.log("Received JSON:", data);
      return Response.json({ success: true, data });
    }
    if (req.method === "POST" && path === "/form") {
      const data = await req.formData();
      console.log(data.get("someField"));
      return new Response("Success");
    }
    return new Response("Page not found", { status: 404 });
  }
});
console.log(`Listening on ${result.url}`);
