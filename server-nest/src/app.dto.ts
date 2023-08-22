import { IsEnum, IsInt } from 'class-validator';

export class Demo {
  name: string;
  @IsInt()
  age: string;
  @IsEnum({ male: 0, female: 1 })
  sex: 'male' | 'female';
}
