import { AppDataSource } from "./data-source";
import { Department } from "./entity/Department";
import { Employee } from "./entity/Employee";

AppDataSource.initialize()
  .then(async () => {
    // const e1 = new Employee();
    // e1.name = "张三";
    // const e2 = new Employee();
    // e2.name = "李四";
    // const e3 = new Employee();
    // e3.name = "王五";
    // const d1 = new Department();
    // d1.name = "技术部";
    // d1.employee = [e1, e2, e3];
    // await AppDataSource.manager.save(Department, d1);
    // const result = await AppDataSource.manager.find(Department, {
    //   relations: {
    //     employee: true,
    //   },
    // });
    // console.log(result);

    // const repository = await AppDataSource.manager.getRepository(Department);

    // const result = await repository
    //   .createQueryBuilder("d")
    //   .leftJoinAndSelect("d.employee", "e")
    //   .getMany();

    const result = await AppDataSource.manager
      .createQueryBuilder(Department, "d")
      .leftJoinAndSelect("d.employee", "e")
      .getMany();
    console.log(result);
  })
  .catch((error) => console.log(error));
