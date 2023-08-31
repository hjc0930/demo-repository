FROM node:latest

WORKDIR /app

COPY . .

RUN npm config set registry https://registry.npmmirror.com/

RUN npm install -g http-server

EXPOSE 8080

# 设定volumn字段，这样可以保证你的容器数据一定会被持久化，防止数据的丢失
VOLUME /app

CMD [ "http-server", "-p", "8080" ]