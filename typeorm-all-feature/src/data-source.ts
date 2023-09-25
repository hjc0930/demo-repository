import "reflect-metadata";
import { DataSource } from "typeorm";
import { User } from "./entity/User";

export const AppDataSource = new DataSource({
  type: "mysql",
  host: "localhost",
  port: 3306,
  username: "root",
  password: "123456",
  database: "practice",
  synchronize: true,
  logging: true,
  entities: [User],
  migrations: [],
  subscribers: [],
  poolSize: 10,
  connectorPackage: "mysql2",
  extra: {
    authPlugin: "sha256_password",
  },
});
