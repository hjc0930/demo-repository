import { Controller, GET } from "fastify-decorators";

@Controller("user")
class UserRouter {
  @GET()
  getHello() {
    return "Hello World";
  }
}

export default UserRouter;
