# 使用官方 Bun 基础镜像（若存在）
FROM oven/bun:1.0-slim

# 设置工作目录
WORKDIR /app

# 复制依赖定义文件（优先利用 Docker 缓存层）
COPY package.json bun.lockb ./

# 安装生产依赖
RUN bun install --production

# 复制项目文件
COPY . .

# 暴露服务端口（根据实际配置调整）
EXPOSE 3000

# 启动命令（根据项目启动脚本调整）
CMD ["bun", "run", "index.ts"]
