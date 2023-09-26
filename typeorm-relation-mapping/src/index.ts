import { AppDataSource } from "./data-source";
import { IdCard } from "./entity/IdCard";
import { User } from "./entity/User";

AppDataSource.initialize()
  .then(async () => {
    // const user = new User();
    // user.firstName = "Minli";
    // user.lastName = "Li";
    // user.age = 24;
    // const idCard = new IdCard();
    // idCard.cardName = "222222";
    // idCard.user = user;
    // await AppDataSource.manager.save(idCard);
    // const ics = await AppDataSource.manager.find(IdCard, {
    //   relations: {
    //     user: true,
    //   },
    // });
    // console.log(ics);
    // const result = await AppDataSource.manager
    //   .getRepository(IdCard)
    //   .createQueryBuilder("IdCard")
    //   .leftJoinAndSelect("IdCard.user", "user")
    //   .getMany();
    // console.log(result);

    // const ics = await AppDataSource.manager
    //   .createQueryBuilder(IdCard, "ic")
    //   .leftJoinAndSelect("ic.user", "u")
    //   .getMany();

    // console.log(ics);

    const result = await AppDataSource.manager.find(User, {
      relations: {
        idCard: true,
      },
    });

    console.log(result);
  })
  .catch((error) => console.log(error));
