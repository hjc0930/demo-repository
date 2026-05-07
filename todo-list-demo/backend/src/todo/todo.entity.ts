import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  DeleteDateColumn,
} from 'typeorm';
import { ApiProperty, ApiHideProperty } from '@nestjs/swagger';

@Entity('todos')
export class Todo {
  @ApiProperty()
  @PrimaryGeneratedColumn({ type: 'int', unsigned: true })
  id: number;

  @ApiProperty()
  @Column({ type: 'varchar', length: 255 })
  title: string;

  @ApiProperty()
  @Column({ type: 'text', default: '' })
  description: string;

  @ApiProperty({ enum: [0, 1, 2] })
  @Column({ type: 'tinyint', default: 0 })
  status: number;

  @ApiProperty({ enum: [0, 1, 2] })
  @Column({ type: 'tinyint', default: 1 })
  priority: number;

  @ApiProperty({ required: false })
  @Column({ type: 'datetime', nullable: true, name: 'due_date' })
  dueDate: Date | null;

  @ApiProperty()
  @CreateDateColumn({ name: 'created_at' })
  createdAt: Date;

  @ApiProperty()
  @UpdateDateColumn({ name: 'updated_at' })
  updatedAt: Date;

  @ApiHideProperty()
  @DeleteDateColumn({ name: 'deleted_at' })
  deletedAt: Date | null;
}
