import {
  Controller,
  Get,
  Body,
  Patch,
  Param,
  Delete,
  Query,
  DefaultValuePipe,
  Inject,
} from '@nestjs/common';
import { StudentService } from './student.service';
import { UpdateStudentDto } from './dto/update-student.dto';

@Controller('student')
export class StudentController {
  @Inject(StudentService)
  private studentService: StudentService;

  @Get('page')
  async findPage(
    @Query('page', new DefaultValuePipe(1))
    page: number,
    @Query('pageSize', new DefaultValuePipe(5))
    pageSize: number,
  ) {
    const result = await this.studentService.findPage(page, pageSize);

    return {
      total: result[1],
      list: result[0],
    };
  }

  @Get(':id')
  async findOne(@Param('id') id: string) {
    const result = await this.studentService.findOne(+id);
    return result;
  }

  @Patch(':id')
  update(@Param('id') id: string, @Body() updateStudentDto: UpdateStudentDto) {
    return this.studentService.update(+id, updateStudentDto);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.studentService.remove(+id);
  }
}
