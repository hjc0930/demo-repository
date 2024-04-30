import fastifyInstance from "fastify";
import process from "node:process";
import { getHello } from "./routers/user";
import readEnvriment from "../config/readEnvriment";

const fastify = fastifyInstance({ logger: true });

fastify.get("/", getHello);

const start = async () => {
  readEnvriment();
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
