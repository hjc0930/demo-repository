import {
  Body,
  Controller,
  Get,
  Inject,
  Post,
  Query,
  Res,
  ValidationPipe,
} from '@nestjs/common';
import { UserService } from './user.service';
import { LoginDto } from './dto/login.dto';
import { RegisterDto } from './dto/register.dto';
import { JwtService } from '@nestjs/jwt';
import { Response } from 'express';

@Controller('user')
export class UserController {
  @Inject(UserService)
  private userService: UserService;
  @Inject(JwtService)
  private jwtService: JwtService;

  @Post('login')
  async login(
    @Body(ValidationPipe) loginDto: LoginDto,
    @Res({ passthrough: true }) res: Response,
  ) {
    const foundUser = await this.userService.login(loginDto);

    if (foundUser) {
      const token = await this.jwtService.signAsync({
        user: {
          id: foundUser.id,
          username: foundUser.username,
        },
      });
      res.setHeader('Authorization', token);
      return 'Login success';
    } else {
      return 'Login failed';
    }
  }

  @Post('register')
  register(@Body(ValidationPipe) registerDto: RegisterDto) {
    return this.userService.register(registerDto);
  }

  @Get()
  async getUserAll() {
    return await this.userService.getUserAll();
  }
  @Get('page')
  async getUserPage(
    @Query('page') page: number,
    @Query('pageSize') pageSize: number,
  ) {
    const result = await this.userService.getUserPage(page, pageSize);
    return {
      code: 200,
      message: 'successful!',
      data: {
        list: result[0],
        total: result[1],
      },
    };
  }
}
