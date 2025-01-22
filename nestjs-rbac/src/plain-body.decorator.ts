import {
  BadRequestException,
  createParamDecorator,
  ExecutionContext,
} from '@nestjs/common';
import type { Request as ExpressRequest } from 'express';
import * as rawBody from 'raw-body';

export const PlainBody = createParamDecorator(
  async (_, context: ExecutionContext) => {
    const req = context.switchToHttp().getRequest<ExpressRequest>();
    if (!req.readable) {
      throw new BadRequestException('Invalid body');
    }

    const body = (await rawBody(req)).toString('utf8').trim();
    return body;
  },
);
