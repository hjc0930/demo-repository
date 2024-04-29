export class UsersRouter {
  constructor(usersService) {
    this.usersService = usersService;
  }

  async getUsers(req, reply) {
    const users = await this.usersService.getUsers();

    return users;
  }
}
