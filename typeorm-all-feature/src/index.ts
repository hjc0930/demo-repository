import { AppDataSource } from "./data-source";
import { User } from "./entity/User";

AppDataSource.initialize().then(async () => {
  // const queryBuilder = AppDataSource.manager.createQueryBuilder();
  // const users = await queryBuilder
  //   .select("user")
  //   .from(User, "user")
  //   .where("user.age = :age", { age: 21 })
  //   .getOne();
  // console.log(users);
  // await AppDataSource.manager.transaction(async (manager) => {
  //   await manager.save(User, {
  //     id: 4,
  //     firstName: "eee",
  //     lastName: "eee",
  //     age: 20,
  //   });
  // });

  const repository = AppDataSource.manager.getRepository(User);

  const result = await repository.find();

  console.log(result);
});
