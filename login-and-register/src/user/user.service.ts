import { HttpException, Injectable, Logger } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { User } from './entities/user.entity';
import { Repository } from 'typeorm';
import { RegisterDto } from './dto/register.dto';
import generatorMd5 from '../utils/generatorMd5';
import { LoginDto } from './dto/login.dto';

@Injectable()
export class UserService {
  private logger = new Logger();

  @InjectRepository(User)
  private userRepository: Repository<User>;

  public async register(registerDto: RegisterDto) {
    const foundUser = await this.userRepository.findOneBy({
      username: registerDto.username,
    });

    if (foundUser) {
      throw new HttpException('用户名已存在', 400);
    }

    const user = new User();
    user.username = registerDto.username;
    user.password = generatorMd5(registerDto.password);

    try {
      await this.userRepository.save(user);
      return 'Register success';
    } catch (error) {
      this.logger.error(error, UserService);
      return 'Register failed';
    }
  }

  public async login(loginDto: LoginDto) {
    const foundUser = await this.userRepository.findOneBy({
      username: loginDto.username,
    });

    if (!foundUser || foundUser.password !== generatorMd5(loginDto.password)) {
      throw new HttpException('用户名或密码错误', 400);
    }

    return foundUser;
  }

  public async getUserAll() {
    return await this.userRepository.find();
  }

  public async getUserPage(page: number, pageSize: number) {
    const queryBuilder = this.userRepository.createQueryBuilder();
    const result = await queryBuilder
      .select()
      .offset((page - 1) * pageSize)
      .limit(pageSize)
      .getManyAndCount();

    return result;
  }
}
