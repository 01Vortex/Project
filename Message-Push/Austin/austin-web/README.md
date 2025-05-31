`austin-web` 是一个 **Web 接口服务模块**，主要承担整个消息推送系统的 **前端交互、接口暴露和后台管理功能**。

---

## 🧩 模块定位

- **作用**：作为系统对外的 API 服务层，提供 RESTful 接口供外部调用。
- **用途**：
    - 提供可视化界面（基于 Amis）进行消息模板配置、渠道账号管理、数据查看等；
    - 对接微信公众号、小程序、支付宝小程序等第三方平台；
    - 处理消息发送请求；
    - 提供日志追踪与链路分析能力；
    - 集成监控、限流、日志采集等功能。
- **技术栈**：
    - Spring Boot
    - Swagger UI
    - MyBatis + JPA
    - WeChat SDK / Alipay MiniProgram SDK
    - Graylog 日志收集
    - Logback 日志框架
    - Prometheus + Micrometer 监控支持

---

## 📦 核心功能详解

### ✅ 接口服务

#### 控制器类（Controller）

| 类名 | 功能 |
|------|------|
| [MessageTemplateController](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\controller\MessageTemplateController.java#L15-L107) | 管理消息模板（增删改查、上下线、预览） |
| [ChannelAccountController](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\controller\ChannelAccountController.java#L14-L98) | 渠道账号管理（短信/邮件/微信公众号等配置） |
| [SendController](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\controller\SendController.java#L16-L72) | 发送消息接口 |
| [DataController](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\controller\DataController.java#L14-L78) | 数据统计与查询接口 |
| [MiniProgramController](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\controller\MiniProgramController.java#L14-L63) | 微信小程序相关接口 |
| [OfficialAccountController](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\controller\OfficialAccountController.java#L14-L56) | 微信公众号回调处理 |
| [AlipayMiniProgramController](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\controller\AlipayMiniProgramController.java#L14-L53) | 支付宝小程序回调处理 |
| [MaterialController](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\controller\MaterialController.java#L14-L62) | 素材上传与管理接口 |
| [HealthController](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\controller\HealthController.java#L14-L34) | 健康检查接口 |

---

### ✅ 前端集成与 Amis 支持

- 使用 [Amis](https://baidu.gitee.io/amis-docs/) 构建低代码前端页面
- 封装工具类 [Convert4Amis](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\utils\Convert4Amis.java#L13-L48) 用于转换后端数据结构为 Amis 兼容格式
- VO 层封装了 Amis 页面所需的结构化数据模型（如 [CommonAmisVo](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\vo\amis\CommonAmisVo.java#L16-L142), [EchartsVo](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\vo\amis\EchartsVo.java#L16-L128), [SmsTimeLineVo](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\vo\amis\SmsTimeLineVo.java#L13-L66) 等）

---

### ✅ 微信生态接入

#### 微信公众号处理

- 实现扫码事件监听处理器 [ScanHandler](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\handler\ScanHandler.java#L13-L61)
- 订阅事件处理 [SubscribeHandler](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\handler\SubscribeHandler.java#L13-L53)
- 取消订阅处理 [UnSubscribeHandler](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\handler\UnSubscribeHandler.java#L13-L47)

#### 微信小程序 & 支付宝小程序

- 登录认证流程控制 [WeChatLoginConfig](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\config\WeChatLoginConfig.java#L13-L37)
- 小程序控制器 [MiniProgramController](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\controller\MiniProgramController.java#L14-L63)
- 支付宝小程序控制器 [AlipayMiniProgramController](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\controller\AlipayMiniProgramController.java#L14-L53)

---

### ✅ 日志与链路追踪

- 使用 Logback 进行日志输出
- 支持将日志发送至 Graylog（test环境）
- 通过注解实现统一响应体封装 [AustinResponseBodyAdvice](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\advice\AustinResponseBodyAdvice.java#L13-L43)
- 统一异常处理 [ExceptionHandlerAdvice](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\exception\ExceptionHandlerAdvice.java#L13-L47)

---

### ✅ 权限与登录管理

- 使用自定义注解实现权限校验
- 登录状态工具类 [LoginUtils](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\utils\LoginUtils.java#L13-L47)
- Token 刷新控制器 [RefreshTokenController](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\controller\RefreshTokenController.java#L14-L39)

---

### ✅ 跨域与全局配置

- 跨域配置类 [CrossConfig](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\config\CrossConfig.java#L13-L37)
- 全局配置类 [CommonConfiguration](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\config\CommonConfiguration.java#L13-L43)

---

### ✅ 文档与调试

- 集成 Swagger UI 接口文档
- 配置类 [SwaggerConfiguration](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\config\SwaggerConfiguration.java#L13-L43)

---

### ✅ 文件上传与资源管理

- 工具类 [SpringFileUtils](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\utils\SpringFileUtils.java#L13-L47)
- 素材上传控制器 [MaterialController](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\controller\MaterialController.java#L14-L62)

---

## 🧪 单元测试与集成测试

- 提供基础测试类 [BaseTestController](file://D:\Code\Project\Message-Push\Austin\austin-web\src\test\java\com\java3y\austin\BaseTestController.java#L13-L43)
- 包含单元测试示例：
    - [EmailTest](file://D:\Code\Project\Message-Push\Austin\austin-web\src\test\java\com\java3y\austin\EmailTest.java#L13-L47)
    - [MiniProgramTestController](file://D:\Code\Project\Message-Push\Austin\austin-web\src\test\java\com\java3y\austin\MiniProgramTestController.java#L13-L43)

---

## 🛠️ 技术细节

### 📁 分层结构清晰

| 包名 | 功能 |
|------|------|
| `advice` | 响应封装、异常处理 |
| `annotation` | 自定义注解（AOP切面） |
| `aspect` | AOP 切面逻辑 |
| [config](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\action\DeduplicationAction.java#L31-L32) | 配置类（跨域、Swagger、登录配置） |
| `controller` | RESTful 接口控制器 |
| `exception` | 异常统一处理 |
| [handler](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\handler\BaseHandler.java#L68-L68) | 第三方平台消息处理（微信公众号扫码、关注等） |
| `service` | 业务逻辑层 |
| `utils` | 工具类（Amis 数据转换、登录工具） |
| `vo` | 视图对象（用于接口返回） |
| `amis` 子包 | Amis 前端所需数据结构封装 |

---

## 🧠 总结

> `austin-web` 是整个消息推送系统的 **前端网关与后台管理模块**，它负责：
- 提供 RESTful 接口供前后端交互；
- 支持 Amis 快速构建低代码后台页面；
- 接入微信公众号、小程序、支付宝小程序等第三方平台；
- 实现日志收集、链路追踪；
- 支持权限控制与登录态管理；
- 提供接口文档（Swagger）；
- 集成健康检查与性能监控（Prometheus）。

它是系统面向用户操作的核心入口，也是连接业务逻辑与前端展示的重要桥梁。