# Spring Admin Soybean

这是一个从 NestJS + TypeScript 迁移到 Spring Boot + Java 的企业级多租户后台管理系统。

## 技术栈

- **Spring Boot 3.2.0** - 主框架
- **Java 17** - 开发语言
- **MyBatis-Plus 3.5.5** - ORM框架
- **PostgreSQL** - 数据库
- **Redis** - 缓存和会话管理
- **Spring Security + JWT** - 安全认证
- **Druid** - 数据库连接池
- **Hutool** - 工具类库

## 项目结构

```
spring-admin-soybean/
├── spring-admin-common/       # 通用模块
│   ├── response/              # Result, ResponseCode, PageResult
│   ├── exception/             # 异常类
│   ├── annotation/            # @RequirePermission, @IgnoreTenant
│   ├── constant/              # 常量
│   └── util/                  # 工具类
├── spring-admin-security/     # 安全模块
│   ├── config/                # SecurityConfiguration
│   ├── filter/                # JwtAuthenticationFilter
│   ├── handler/               # 认证/授权处理器
│   └── utils/                 # JwtUtils
├── spring-admin-tenant/       # 多租户模块
│   ├── context/               # TenantContextHolder
│   ├── interceptor/           # 租户拦截器
│   └── config/                # 租户配置
├── spring-admin-data/         # 数据访问模块
│   ├── entity/                # 实体类
│   ├── mapper/                # Mapper接口
│   └── config/                # MyBatis-Plus配置
├── spring-admin-system/       # 系统管理模块
│   ├── controller/            # 用户、角色、菜单控制器
│   ├── service/               # 业务逻辑
│   └── dto/                   # 数据传输对象
├── spring-admin-monitor/      # 监控模块
├── spring-admin-generator/    # 代码生成模块
└── spring-admin-app/          # 启动模块
```

## 核心功能

### 1. 多租户支持
- 基于 ThreadLocal 的租户上下文管理
- MyBatis-Plus 多租户插件自动注入租户ID
- @IgnoreTenant 注解支持忽略租户过滤
- 租户白名单配置

### 2. 安全认证
- JWT 认证（Access Token + Refresh Token）
- Token 黑名单（Redis 实现）
- 登录失败锁定
- API 限流
- RBAC 权限控制

### 3. API 兼容性
- 路径保持一致：`/api` 前缀
- 响应格式：`{code, msg, data, requestId, timestamp}`
- 成功码：`200`
- Token 传递：`Authorization: Bearer {token}`

### 4. 数据库表结构
复用原有数据库表结构，支持：
- 租户管理
- 用户权限
- 系统配置
- 字典管理
- 文件管理
- 代码生成
- 消息通知
- 定时任务

## 快速开始

### 1. 环境准备
- JDK 17+
- Maven 3.6+
- PostgreSQL 12+
- Redis 6+

### 2. 数据库初始化
```sql
-- 创建数据库
CREATE DATABASE soybean_admin;

-- 执行原项目的数据库迁移脚本
```

### 3. 配置修改
修改 `spring-admin-app/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/soybean_admin
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
```

### 4. 启动应用
```bash
cd spring-admin-app
mvn spring-boot:run
```

### 5. 访问应用
- API 地址：http://localhost:8080/api
- Druid 监控：http://localhost:8080/druid
- Actuator：http://localhost:8080/actuator

## API 示例

### 登录
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123",
  "tenantId": "000000"
}
```

### 获取用户信息
```bash
GET /api/auth/user-info
Authorization: Bearer {access_token}
```

### 用户列表
```bash
GET /api/system/user/page?pageNum=1&pageSize=10
Authorization: Bearer {access_token}
```

## 开发指南

### 添加新的实体类
1. 在 `spring-admin-data/src/main/java/com/soybean/admin/data/entity/` 创建实体类
2. 继承 `BaseEntity`，使用 MyBatis-Plus 注解
3. 在 `spring-admin-data/src/main/java/com/soybean/admin/data/mapper/` 创建 Mapper 接口

### 添加新的API
1. 在相应的模块创建 Controller
2. 使用 `@RequirePermission` 注解控制权限
3. 使用 `Result.ok()` 或 `Result.fail()` 返回统一响应

### 多租户使用
```java
// 获取当前租户ID
String tenantId = TenantContextHolder.getTenantId();

// 忽略租户过滤
@IgnoreTenant
public List<SysUser> getAllUsers() {
    return userMapper.selectList(null);
}

// 临时切换租户
TenantContextHolder.runWithTenant("000001", () -> {
    // 在此租户下执行操作
    return doSomething();
});
```

## 注意事项

1. **API 兼容性**：所有 API 端点与原 NestJS 项目保持一致
2. **响应格式**：严格遵循 `{code, msg, data, requestId, timestamp}` 格式
3. **租户隔离**：所有业务数据表都应包含 `tenant_id` 字段
4. **逻辑删除**：使用 `del_flag` 字段，0 表示正常，1 表示删除
5. **时间字段**：统一使用 `LocalDateTime` 类型

## 许可证

MIT License
