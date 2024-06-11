import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  DeleteDateColumn,
} from 'typeorm';

@Entity()
export class Student {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({
    length: 20,
  })
  name: string;

  @Column({
    length: 20,
  })
  gender: string;

  @Column()
  age: number;

  @Column({
    length: 20,
  })
  class: string;

  @Column()
  score: number;

  @CreateDateColumn({
    type: 'datetime',
  })
  createAt: Date;
  @UpdateDateColumn({
    type: 'datetime',
  })
  updateAt: Date;
  @DeleteDateColumn({
    type: 'datetime',
  })
  deleteAt: Date;
}
