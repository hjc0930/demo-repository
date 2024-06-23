import { Injectable } from '@nestjs/common';
import { UpdateStudentDto } from './dto/update-student.dto';
import { Student } from './entities/student.entity';
import { Repository } from 'typeorm';
import { InjectRepository } from '@nestjs/typeorm';

@Injectable()
export class StudentService {
  @InjectRepository(Student)
  private studentRepository: Repository<Student>;

  findPage(page: number, pageSize: number) {
    return this.studentRepository.findAndCount({
      skip: (page - 1) * pageSize,
      take: pageSize,
      order: {
        id: 'DESC',
      },
      where: {
        deleteAt: null,
      },
    });
  }

  findOne(id: number) {
    return this.studentRepository.findOneBy({ id, deleteAt: null });
  }

  update(id: number, updateStudentDto: UpdateStudentDto) {
    this.studentRepository.update(id, updateStudentDto);
    // return `This action updates a #${id} student`;
  }

  remove(id: number) {
    this.studentRepository.update(id, { deleteAt: new Date() });
  }
}
