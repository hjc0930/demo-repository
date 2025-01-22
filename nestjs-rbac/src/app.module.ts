import { Module } from '@nestjs/common';
import { AppController } from './app.controller';

@Module({
  imports: [],
  controllers: [AppController],
  providers: [],
})
export class AppModule {
  // configure(consumer: MiddlewareConsumer) {
  //   consumer.apply(PlainTextMiddleware).forRoutes({
  //     path: 'body',
  //     method: RequestMethod.POST,
  //   });
  // }
}

// @Injectable()
// export class PlainTextMiddleware implements NestMiddleware {
//   use(req: Request, res: Response, next: NextFunction) {
//     if (req.is('text/plain')) {
//       let data = '';
//       req.setEncoding('utf8');
//       req.on('data', (chunk) => {
//         data += chunk;
//       });
//       req.on('end', () => {
//         req.body = data;
//         next();
//       });
//     } else {
//       next();
//     }
//   }
// }
