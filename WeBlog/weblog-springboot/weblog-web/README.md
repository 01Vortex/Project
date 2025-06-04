`weblog-module-jwt` 是一个基于 **JWT（JSON Web Token）** 的认证模块，主要用于实现博客系统的用户登录、身份验证、Token 生成与校验等功能。该模块作为 Spring Security 的扩展组件，为系统提供安全访问控制和无状态的认证机制。

---

## 模块功能概述

### 1. **JWT 认证流程集成**
- 实现完整的 JWT 登录认证流程：
    - 用户提交用户名和密码进行登录。
    - 系统验证凭证后生成 JWT Token。
    - 后续请求携带 Token 进行身份验证和权限校验。

> 示例类：[JwtAuthenticationLoginFilter](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-jwt\src\main\java\com\quanxiaoha\weblog\jwt\JwtAuthenticationLoginFilter.java#L20-L46)

---

### 2. **Token 生成与解析**
- 使用 `jjwt` 库实现 Token 的签发与验证。
- 支持自定义签发人（issuer）、过期时间、签名密钥等配置。
- 提供工具方法用于从 Token 中提取用户名、判断是否过期等。

> 核心类：[JwtTokenHelper](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-jwt\src\main\java\com\quanxiaoha\weblog\jwt\JwtTokenHelper.java#L35-L125)

---

### 3. **认证成功/失败处理器**
- 自定义登录成功后的 Token 返回逻辑。
- 统一处理登录失败（如用户名或密码错误）并返回标准化响应。

| 类名 | 功能 |
|------|------|
| [LoginAuthenticationSuccessHandler](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-jwt\src\main\java\com\quanxiaoha\weblog\jwt\LoginAuthenticationSuccessHandler.java#L20-L46) | 登录成功生成 Token 并返回 |
| [LoginAuthenticationFailureHandler](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-jwt\src\main\java\com\quanxiaoha\weblog\jwt\LoginAuthenticationFailureHandler.java#L20-L38) | 处理登录失败（用户名或密码错误） |

---

### 4. **Token 校验过滤器**
- 在每次请求时拦截 Token，验证其有效性，并将用户信息注入到 Spring Security 上下文中。
- 支持配置 Token 前缀和 Header 名称。

> 核心类：[TokenAuthenticationFilter](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-jwt\src\main\java\com\quanxiaoha\weblog\jwt\TokenAuthenticationFilter.java#L27-L76)

---

### 5. **异常统一处理**
- 自定义未登录访问受保护资源时的响应。
- 自定义权限不足时的响应。

| 类名 | 功能 |
|------|------|
| [RestAuthenticationEntryPoint](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-jwt\src\main\java\com\quanxiaoha\weblog\jwt\RestAuthenticationEntryPoint.java#L20-L37) | 未登录访问返回 401 |
| [RestAccessDeniedHandler](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-jwt\src\main\java\com\quanxiaoha\weblog\jwt\RestAccessDeniedHandler.java#L19-L30) | 权限不足返回 403 |

---

### 6. **安全配置集成**
- 配置 JWT 登录过滤器加入 Spring Security 流程。
- 设置 UserDetailsService 和加密方式（PasswordEncoder）。

> 核心类：[JwtAuthenticationSecurityConfig](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-jwt\src\main\java\com\quanxiaoha\weblog\jwt\JwtAuthenticationSecurityConfig.java#L18-L62)

---

### 7. **数据模型与响应封装**
- 请求参数封装：
    - [CreateTokenReqVO](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-jwt\src\main\java\com\quanxiaoha\weblog\jwt\model\CreateTokenReqVO.java#L12-L20)
- 响应结果封装：
    - [CreateTokenRspVO](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-jwt\src\main\java\com\quanxiaoha\weblog\jwt\model\CreateTokenRspVO.java#L12-L19)

---

### 8. **通用响应工具类**
- 提供 HTTP 响应写入工具，用于向前端返回 JSON 格式的认证结果。

> 工具类：[ResultUtil](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-jwt\src\main\java\com\quanxiaoha\weblog\jwt\utils\ResultUtil.java#L14-L41)

---

## 模块结构概览

```
src/main/java/
├── model/                      // 请求/响应模型
│   ├── CreateTokenReqVO.java     // 登录请求参数
│   └── CreateTokenRspVO.java     // 登录响应参数
├── utils/                      // 工具类
│   └── ResultUtil.java           // 响应输出工具
├── JwtAuthenticationLoginFilter.java // 登录认证过滤器
├── JwtTokenHelper.java         // JWT 工具类
├── LoginAuthenticationSuccessHandler.java // 登录成功处理器
├── LoginAuthenticationFailureHandler.java // 登录失败处理器
├── TokenAuthenticationFilter.java      // Token 校验过滤器
├── RestAuthenticationEntryPoint.java   // 未登录访问入口点处理器
├── RestAccessDeniedHandler.java        // 权限不足处理器
└── JwtAuthenticationSecurityConfig.java // 安全配置类
```


---

## Maven 依赖说明

```xml
<dependencies>
    <!-- 公共模块 -->
    <dependency>
        <groupId>com.quanxiaoha</groupId>
        <artifactId>weblog-module-common</artifactId>
    </dependency>

    <!-- Spring Boot Web & Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- JWT 相关 -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```


---

##  总结

`weblog-module-jwt` 是整个博客系统中负责 **用户认证和权限控制** 的核心模块，具备以下特点：

| 功能类别 | 描述 |
|----------|------|
| 登录认证 | 提供基于 JWT 的登录认证机制 |
| Token 管理 | 支持 Token 的生成、解析、校验 |
| 安全集成 | 与 Spring Security 深度集成 |
| 异常处理 | 统一处理未授权访问和权限不足 |
| 响应封装 | 提供标准格式的请求响应 |
| 可扩展性 | 支持后续接入 OAuth2、多租户等扩展场景 |

它为前后台模块提供了统一的安全访问接口，确保系统在高并发和分布式环境下具备良好的安全性与可维护性。