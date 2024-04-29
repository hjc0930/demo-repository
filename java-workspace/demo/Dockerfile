# 使用基础的Java 21镜像
FROM amazoncorretto:21-alpine3.16

# 设置容器的工作目录
WORKDIR /app

# 将本地文件添加到容器中
COPY target/demo-1.0.0.jar .

# 暴露应用程序的端口
EXPOSE 8080

# 在容器启动时运行应用程序
CMD ["java", "-jar", "demo-1.0.0.jar"]
