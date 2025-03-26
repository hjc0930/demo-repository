import { serve } from "bun";
import { UserController } from "./controllers/user.controller";

const userController = new UserController();

const result = serve({
  routes: {
    "/": new Response("It's work!!!"),
    "/user": {
      GET: userController.getUser,
    },
  },
});

console.log(`Listening on ${result.url}`);
