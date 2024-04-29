import { Controller, Get, Inject } from '@nestjs/common';
import { CityService } from './city.service';

@Controller('city')
export class CityController {
  @Inject(CityService)
  private cityService: CityService;

  @Get()
  findAll() {
    return this.cityService.findAll();
  }
}
