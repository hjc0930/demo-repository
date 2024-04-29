import { Injectable } from '@nestjs/common';
import { InjectEntityManager } from '@nestjs/typeorm';
import { EntityManager } from 'typeorm';
import { City } from './entities/city.entity';

@Injectable()
export class CityService {
  @InjectEntityManager()
  private entityManager: EntityManager;

  async findAll() {
    const city = new City();
    city.name = '华南';
    await this.entityManager.save(city);

    const cityChild1 = new City();
    cityChild1.name = '云南';
    const parent = await this.entityManager.findOne(City, {
      where: {
        name: '华南',
      },
    });
    if (parent) {
      cityChild1.parent = parent;
    }
    await this.entityManager.save(City, cityChild1);

    const cityChild2 = new City();
    cityChild2.name = '昆明';

    const parent2 = await this.entityManager.findOne(City, {
      where: {
        name: '云南',
      },
    });
    if (parent) {
      cityChild2.parent = parent2;
    }
    await this.entityManager.save(City, cityChild2);

    return this.entityManager.getTreeRepository(City).findTrees();
  }
}
