// import fastifyInstance from "fastify";
import { UsersRouter } from "./routers/usersRouter.js";
import { UserServices } from "./services/userServices.js";

// const fastify = fastifyInstance({ logger: true });

const userServices = new UserServices();
const usersRouter = Reflect.construct(UsersRouter, [userServices]);

console.log(Object.getOwnPropertyNames(usersRouter));

// fastify.route({
//   method: "GET",
//   url: "/users",
//   handler: (request, reply) => usersRouter.getUsers(request, reply),
// });

// try {
//   await fastify.listen({
//     port: 3000,
//   });
//   fastify.log.info(`server listening on ${fastify.server.address().port}`);
// } catch (err) {
//   fastify.log.error(err);
//   process.exit(1);
// }
