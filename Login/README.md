# 🚀 Login - Spring Boot 登录注册功能模板

基于 Spring Boot 的登录认证系统，集成安全控制、数据库访问、邮件与短信验证、Redis 缓存等现代后端开发功能。

---

## 📌 项目简介

本项目是一个基于 **Spring Boot 3.4.5** 构建的完整登录系统，支持用户注册、登录、权限管理、邮箱验证、短信验证码以及 Redis 缓存优化等功能。适用于企业级后台系统的快速搭建和学习参考。

---

## 🔧 技术栈

| 技术 | 描述 |
|------|------|
| Spring Boot | 快速构建微服务基础框架 |
| Spring Security | 安全控制、权限认证 |
| Spring Data JPA / MyBatis | 数据库访问 |
| MySQL | 关系型数据库支持 |
| Thymeleaf | 后台模板引擎渲染页面（可选） |
| Spring Mail | 邮件发送（如注册验证） |
| Aliyun SMS SDK | 短信验证码支持 |
| Redis | 用户会话缓存与验证码存储 |
| Lombok | 减少样板代码编写 |
| Hibernate Validator | 参数校验支持 |

---

## 📦 项目结构概览

```
Login/
├── src/
│   ├── main/
│   │   ├── java/              # Java 源码目录
│   │   └── resources/
│   │       ├── application.properties # 配置文件
│   │       └── banner.txt             # 自定义启动 Banner
│   └── test/                  # 单元测试
├── pom.xml                    # Maven 构建配置
└── README.md                  # 项目说明文档
```


---

## ✅ 功能亮点

- ✅ 用户注册 & 登录流程
- ✅ 邮箱验证机制（防止恶意注册）
- ✅ 短信验证码登录（集成阿里云短信服务）
- ✅ Spring Security 权限控制
- ✅ 使用 Redis 缓存验证码和用户状态
- ✅ 支持 Thymeleaf 页面渲染（可选前端模块）

---

## 🛠️ 如何运行

### 1. 克隆项目

```bash
git clone https://github.com/yourname/Login.git
cd Login
```


### 2. 修改配置文件

编辑 `src/main/resources/application.properties` 文件，根据你的环境修改数据库、Redis、邮件、短信等配置。

### 3. 构建并运行项目

```bash
mvn clean install
mvn spring-boot:run
```


或者使用 IDE（如 IntelliJ IDEA）直接运行主类 [Application.java](file://D:\Code\Project\Login\src\main\java\com\example\login\LoginApplication.java)。

---

## 🌐 接口文档（待完善）

你也可以通过 Swagger 或 SpringDoc 查看 API 接口文档（如果已集成）。

---

## 📬 联系方式

如有问题或建议，请提交 Issues 或联系作者。

---

## 📜 许可证

本项目采用 [MIT License](LICENSE) 开源协议。

--- 

你可以将以上内容保存为 `README.md` 文件，并提交到项目的 GitHub 仓库中。如果你有更多模块、部署说明或截图，可以进一步扩展此文档。需要我帮你生成更详细的部署说明、API 接口文档结构或 `.github/CONTRIBUTING.md` 模板，也可以告诉我！
