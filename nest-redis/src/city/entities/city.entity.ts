import {
  Column,
  CreateDateColumn,
  Entity,
  PrimaryGeneratedColumn,
  Tree,
  TreeChildren,
  TreeParent,
  UpdateDateColumn,
} from 'typeorm';

@Entity()
@Tree('closure-table')
export class City {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ default: 0 })
  status: number;

  @Column()
  name: string;

  @CreateDateColumn()
  createDate: Date;

  @UpdateDateColumn()
  updateDate: Date;

  @TreeChildren()
  children: City[];

  @TreeParent()
  parent: City;
}
