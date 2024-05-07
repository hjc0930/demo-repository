import fastifyInstance from "fastify";
import { bootstrap } from "fastify-decorators";
import process from "node:process";
import { readdir } from "fs/promises";
import UserRouter from "./routers/user";

const fastify = fastifyInstance({ logger: true });
fastify.register(bootstrap, {
  controllers: [UserRouter],
});
fastify;

fastify.register(import("@fastify/cors"), {
  origin: "*",
});

const start = async () => {
  const files = await readdir("./src/routers");
  files.forEach(async (file) => {
    const router = await import(`./routers/${file}`);

    const re: UserRouter = Reflect.construct(router.default, []);
    console.log(re.getHello());

    fastify.register(router.default);
  });

  try {
    await fastify.listen({
      port: 3000,
    });
    fastify.log.info(
      `server listening on ${(fastify.server.address() as any).port}`
    );
  } catch (err) {
    fastify.log.error(err);
    process.exit(1);
  }
};
start();
