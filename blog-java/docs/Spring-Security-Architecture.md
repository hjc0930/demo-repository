# Spring Security 架构详解

## 一、核心概念

| 概念 | 说明 |
|------|------|
| **Authentication（认证）** | 验证"你是谁"（登录） |
| **Authorization（授权）** | 验证"你能做什么"（权限检查） |

---

## 二、架构层次

```
┌─────────────────────────────────────────────────────────────┐
│                        应用层                                │
│  Controller → Service → Mapper                               │
└─────────────────────────────────────────────────────────────┘
                              ↑
                              │ 经过认证和授权后
                              │
┌─────────────────────────────────────────────────────────────┐
│              Spring Security 过滤器链（按执行顺序）           │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  ① 认证阶段 Filters                                  │  │
│  │  ┌────────────────────────────────────────────────┐  │  │
│  │  │ • DelegatingFilterProxy (入口)                 │  │  │
│  │  │ • FilterChainProxy (管理过滤器链)              │  │  │
│  │  │ • UsernamePasswordAuthenticationFilter         │  │  │
│  │  │ • JwtAuthenticationFilter (自定义)            │  │  │
│  │  │   ↓ 调用认证管理器                              │  │  │
│  │  │ • AuthenticationManager                        │  │  │
│  │  │   └─ AuthenticationProvider                    │  │  │
│  │  │   └─ UserDetailsService                        │  │  │
│  │  │   └─ PasswordEncoder                           │  │  │
│  │  └────────────────────────────────────────────────┘  │  │
│  │                    ↓ 认证成功                          │  │
│  │  ┌────────────────────────────────────────────────┐  │  │
│  │  │ ② 授权阶段 Filters                              │  │  │
│  │  │ • FilterSecurityInterceptor (权限拦截)         │  │  │
│  │  │   └─ AccessDecisionManager                     │  │  │
│  │  │   └─ SecurityContextHolder (获取用户信息)      │  │  │
│  │  └────────────────────────────────────────────────┘  │  │
│  │                    ↓ 授权通过                          │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                             │
│  SecurityContext (ThreadLocal 存储当前会话的认证信息)        │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                        数据层                                │
│                      User / Role / Permission                │
└─────────────────────────────────────────────────────────────┘
```

---

## 三、核心组件

### 3.1 过滤器链（执行顺序）

```
HTTP 请求
    ↓
┌──────────────────────────────────────────────────────────────┐
│                    第一阶段：通用前置处理                      │
│  1. ChannelProcessingFilter      (HTTPS 协议检查)            │
│  2. WebAsyncManagerIntegrationFilter                        │
│  3. SecurityContextHolderFilter   (从请求恢复 SecurityContext)│
│  4. HeaderWriterFilter            (添加响应头)                │
│  5. CorsFilter                    (跨域处理)                 │
│  6. CsrfFilter                    (CSRF 防护)                │
└──────────────────────────────────────────────────────────────┘
    ↓
┌──────────────────────────────────────────────────────────────┐
│                    第二阶段：认证（你是谁？）                   │
│  7. LogoutFilter                  (退出登录)                  │
│  8. UsernamePasswordAuthenticationFilter  (表单登录认证)       │
│     ↓ 调用 AuthenticationManager                              │
│  9. JwtAuthenticationFilter (自定义)      (JWT Token 认证)    │
│     ↓ 验证 Token → 解析用户信息 → 存入 SecurityContext        │
└──────────────────────────────────────────────────────────────┘
    ↓
┌──────────────────────────────────────────────────────────────┐
│                    第三阶段：授权（你能做什么？）               │
│  10. FilterSecurityInterceptor        (URL 权限拦截)          │
│      ↓ 从 SecurityContext 获取已认证用户信息                   │
│      ↓ AccessDecisionManager 决策是否允许访问                 │
│      ↓ 通过 → 继续；不通过 → 抛出 AccessDeniedException       │
└──────────────────────────────────────────────────────────────┘
    ↓
┌──────────────────────────────────────────────────────────────┐
│                    第四阶段：业务逻辑                          │
│  Controller → Service → Mapper                                │
└──────────────────────────────────────────────────────────────┘
```

**关键点**：认证必须先于授权完成，否则 `FilterSecurityInterceptor` 无法获取用户信息进行权限判断。

### 3.2 认证核心组件

| 组件 | 职责 |
|------|------|
| **AuthenticationManager** | 认证管理器入口 |
| **AuthenticationProvider** | 具体认证逻辑提供者 |
| **UserDetailsService** | 从数据库加载用户信息 |
| **PasswordEncoder** | 密码加密/验证 |

```
AuthenticationManager
        ↓
    DaoAuthenticationProvider
        ↓
    UserDetailsService.loadUserByUsername()
        ↓
    返回 UserDetails (包含用户名、密码、权限)
        ↓
    PasswordEncoder.matches(原始密码, 数据库密码)
        ↓
    认证成功，存储到 SecurityContext
```

### 3.3 授权核心组件

| 组件 | 职责 |
|------|------|
| **SecurityContextHolder** | 存储当前用户认证信息 |
| **GrantedAuthority** | 用户权限（角色） |
| **AccessDecisionManager** | 访问决策管理器 |
| **FilterSecurityInterceptor** | URL 权限拦截器 |

### 3.4 核心对象关系

```
Authentication (认证对象)
    ├── principal       → 主体（用户名/UserDetails）
    ├── credentials     → 凭证（密码，认证后清空）
    ├── authorities     → 权限列表（角色）
    └── details         → 详情（Web认证信息等）

            ↓
  SecurityContextHolder
    └── SecurityContext
            └── Authentication
```

---

## 四、认证流程

```
用户提交登录信息
        ↓
UsernamePasswordAuthenticationFilter
        ↓
AuthenticationManager.authenticate()
        ↓
DaoAuthenticationProvider
        ↓
UserDetailsService.loadUserByUsername()
        ↓
从数据库查询用户 → 返回 UserDetails
        ↓
PasswordEncoder.matches(输入密码, 数据库密码)
        ↓
认证成功 → 创建 Authentication(包含用户信息 + 权限)
        ↓
SecurityContextHolder.getContext().setAuthentication()
        ↓
后续请求可从 SecurityContext 获取用户信息
```

---

## 五、JWT 认证流程（本项目）

### 5.1 登录流程

```
登录请求
    ↓
AuthController (自定义)
    ↓
验证用户名密码 → 调用 AuthenticationManager
    ↓
认证成功 → 生成 JWT Token
    ↓
返回 Token 给客户端
```

### 5.2 请求认证流程

```
后续请求
    ↓
JwtAuthenticationFilter (自定义)
    ↓
从请求头提取 Token → 验证有效性
    ↓
从 Token 解析用户信息
    ↓
UserDetailsService.loadUserByUsername()
    ↓
创建 Authentication → 存入 SecurityContext
    ↓
请求到达 Controller
```

---

## 六、关键设计模式

| 模式 | 应用 |
|------|------|
| **责任链模式** | FilterChain 过滤器链 |
| **委托模式** | DelegatingFilterProxy 委托给 Spring Bean |
| **策略模式** | AuthenticationProvider 多种认证方式 |
| **模板方法** | AbstractAuthenticationToken |

---

## 七、本项目 Security 组件结构

```
config/
└── SecurityConfig.java              ← 主配置

security/
├── filter/
│   └── JwtAuthenticationFilter.java ← JWT 过滤器
├── handler/
│   ├── JwtAuthenticationEntryPoint.java  ← 401 处理
│   └── JwtAccessDeniedHandler.java      ← 403 处理
├── service/
│   └── CustomUserDetailsService.java    ← 用户加载
├── user/
│   └── BlogUserDetails.java             ← 自定义 UserDetails
└── util/
    └── SecurityContextUtil.java         ← 工具类

common/utils/
└── JwtUtil.java                         ← JWT 工具
```

---

## 八、UsernamePasswordAuthenticationToken 详解

### 构造函数

```java
// 未认证场景（登录时）
public UsernamePasswordAuthenticationToken(
    Object principal,    // 用户名
    Object credentials   // 密码
)

// 已认证场景（认证成功后）
public UsernamePasswordAuthenticationToken(
    Object principal,                        // UserDetails
    Object credentials,                     // null（安全考虑）
    Collection<? extends GrantedAuthority>  // 权限列表
)
```

### 参数说明

| 参数 | 未认证时 | 已认证时 | 说明 |
|------|----------|----------|------|
| **principal** | 用户名 | UserDetails | 主体 |
| **credentials** | 密码 | `null` | 凭证 |
| **authorities** | `null` | 权限列表 | 角色/权限 |

### 为什么认证后 credentials 为 null？

1. **已认证** - 用户已通过验证，不需要保留密码
2. **安全** - 避免密码在内存中泄露
3. **Spring Security 规范** - 标准做法

---

## 九、异常处理器体系

### 9.1 处理器分类

```
┌─────────────────────────────────────────────────────────────┐
│                    异常处理体系                               │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ① 安全异常处理（过滤器链异常）                               │
│     • AuthenticationEntryPoint  ← 本项目已用 (401)           │
│     • AccessDeniedHandler         ← 本项目已用 (403)           │
│                                                             │
│  ② 认证流程处理                                              │
│     • AuthenticationSuccessHandler  (认证成功)               │
│     • AuthenticationFailureHandler  (认证失败)               │
│                                                             │
│  ③ 登出流程处理                                              │
│     • LogoutSuccessHandler          (登出成功)               │
│                                                             │
│  ④ 会话管理处理                                              │
│     • SessionAuthenticationStrategy  (会话策略)              │
│     • InvalidSessionStrategy         (无效会话)              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 9.2 安全异常处理器

| 处理器 | 触发时机 | 触发异常 | 本项目实现 |
|--------|----------|----------|------------|
| **AuthenticationEntryPoint** | 认证失败（未登录/TOKEN无效） | `AuthenticationException` | `JwtAuthenticationEntryPoint` |
| **AccessDeniedHandler** | 授权失败（已登录但无权限） | `AccessDeniedException` | `JwtAccessDeniedHandler` |

**执行位置**：`ExceptionTranslationFilter`（过滤器链中捕获异常）

```
过滤器链执行流程
    ↓
认证/授权 Filter 抛出异常
    ↓
ExceptionTranslationFilter 捕获
    ↓
┌─────────────────────────────────────────┐
│ AuthenticationException?               │
│   → AuthenticationEntryPoint.commence() │
│     → 返回 401                          │
├─────────────────────────────────────────┤
│ AccessDeniedException?                 │
│   → AccessDeniedHandler.handle()       │
│     → 返回 403                          │
└─────────────────────────────────────────┘
```

### 9.3 认证流程处理器

| 处理器 | 触发时机 | 配置方式 | 本项目使用 |
|--------|----------|----------|------------|
| **AuthenticationSuccessHandler** | 认证成功 | `formLogin().successHandler()` | ❌ 不使用（自定义Controller） |
| **AuthenticationFailureHandler** | 认证失败 | `formLogin().failureHandler()` | ❌ 不使用（自定义Controller） |

**注意**：本项目使用 JWT + 自定义 `AuthController`，不需要这两个处理器。

### 9.4 登出流程处理器

| 处理器 | 触发时机 | 配置方式 |
|--------|----------|----------|
| **LogoutSuccessHandler** | 登出成功 | `logout().logoutSuccessHandler()` |

**注意**：JWT 无状态架构，登出通常由客户端删除 Token 实现。

### 9.5 会话管理处理器

| 处理器/策略 | 触发时机 | 说明 |
|-------------|----------|------|
| **SessionAuthenticationStrategy** | 认证成功后 | 处理会话固定保护、并发控制等 |
| **InvalidSessionStrategy** | Session 无效时 | 处理过期/无效 Session（SessionID 无效） |

**注意**：本项目使用 `SessionCreationPolicy.STATELESS`，不依赖 Session。

### 9.6 配置示例

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // ① 安全异常处理
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((request, response, authException) -> {
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"msg\":\"未认证\"}");
            })
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"msg\":\"无权限\"}");
            })
        )

        // ② 认证流程处理（表单登录场景）
        .formLogin(form -> form
            .successHandler((request, response, authentication) -> {
                response.getWriter().write("登录成功");
            })
            .failureHandler((request, response, exception) -> {
                response.setStatus(401);
                response.getWriter().write("登录失败");
            })
        )

        // ③ 登出处理
        .logout(logout -> logout
            .logoutSuccessHandler((request, response, authentication) -> {
                response.getWriter().write("登出成功");
            })
        );

    return http.build();
}
```

### 9.7 本项目异常处理总结

```
本项目（JWT 无状态）：
  ✓ AuthenticationEntryPoint  → JwtAuthenticationEntryPoint (401)
  ✓ AccessDeniedHandler      → JwtAccessDeniedHandler (403)
  ✗ AuthenticationSuccessHandler  → 不需要（自定义 AuthController）
  ✗ AuthenticationFailureHandler  → 不需要（自定义 AuthController）
  ✗ LogoutSuccessHandler          → 不需要（客户端删除 Token）
  ✗ Session相关处理器              → 不需要（STATELESS 模式）
```

---

## 十、关键点总结

1. **过滤器链** - 所有请求先经过 Security 过滤器链
2. **认证** - AuthenticationManager 负责验证身份
3. **授权** - 通过 authorities/roles 进行权限控制
4. **SecurityContext** - 使用 ThreadLocal 存储当前用户信息
5. **JWT 扩展** - 自定义 Filter 在过滤器链中处理 Token

---

## 十一、参考资料

- [Spring Security 官方文档](https://docs.spring.io/spring-security/reference/)
- [Spring Boot + Security 教程](https://spring.io/guides/gs/securing-web/)
