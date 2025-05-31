`austin-support` 是一个 **通用支持模块**，主要为整个消息推送系统提供基础设施能力，包括：

- 消息队列（Kafka、RabbitMQ、Redis、RocketMQ、EventBus）的抽象与实现；
- 线程池管理；
- 配置读取（支持本地 properties、Nacos、Apollo）；
- Redis 工具封装；
- HTTP 请求工具类；
- 日志打印与链路追踪；
- Token 获取与缓存；
- 账号信息管理；
- 文件处理等。

---

## 🧩 模块定位

- **作用**：提供基础组件和工具类，供其他模块调用。
- **用途**：
    - 消息发送统一接口；
    - 多种 MQ 实现支持；
    - 动态配置中心对接；
    - 日志记录；
    - 第三方 token 获取；
    - 线程池管理；
    - Redis 操作封装；
    - URL 构建与参数替换；
    - 文件下载处理等。
- **技术栈**：
    - Spring Boot
    - Apache Kafka
    - RabbitMQ / RocketMQ / Redis
    - Apollo / Nacos
    - Hutool / Guava / FastJSON
    - Lettuce / OkHttp / DingTalk SDK / WeChat SDK

---

## 📦 核心功能详解

### ✅ 消息队列抽象与多实现

#### 接口定义

```java
public interface SendMqService {
    void send(String topic, String jsonValue, String tagId);
    void send(String topic, String jsonValue);
}
```


#### 支持的消息队列类型

| 类名 | 对应消息队列 |
|------|----------------|
| [EventBusSendMqServiceImpl](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\mq\eventbus\EventBusSendMqServiceImpl.java#L19-L53) | Google Guava EventBus（单机使用） |
| [SpringEventBusSendMqServiceImpl](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\mq\springeventbus\SpringEventBusSendMqServiceImpl.java#L16-L36) | Spring Event（Spring上下文事件） |
| [KafkaSendMqServiceImpl](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\mq\kafka\KafkaSendMqServiceImpl.java#L24-L49) | Apache Kafka |
| [RabbitSendMqServiceImpl](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\mq\rabbit\RabbitSendMqServiceImpl.java#L20-L68) | RabbitMQ |
| [RedisSendMqServiceImpl](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\mq\redis\RedisSendMqServiceImpl.java#L20-L62) | Redis List（模拟MQ） |
| [RocketMqSendMqServiceImpl](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\mq\rocketmq\RocketMqSendMqServiceImpl.java#L18-L38) | RocketMQ |

#### 使用方式

通过 `@ConditionalOnProperty(name = "austin.mq.pipeline", havingValue = MessageQueuePipeline.KAFKA)` 控制启用哪个实现。

---

### ✅ 线程池管理

- 提供线程池配置类 [SupportThreadPoolConfig](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\config\SupportThreadPoolConfig.java#L14-L35) 和优雅关闭机制 [ThreadPoolExecutorShutdownDefinition](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\config\ThreadPoolExecutorShutdownDefinition.java#L19-L60)
- 支持延迟消费队列 `AbstractLazyPending<T>`
- 支持动态线程池注册 [ThreadPoolUtils](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\utils\ThreadPoolUtils.java#L13-L28)

---

### ✅ 配置中心集成

- 支持从以下配置中心获取配置：
    - Apollo（[ConfigServiceImpl](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\service\impl\ConfigServiceImpl.java#L18-L54)）
    - Nacos（[NacosUtils](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\utils\NacosUtils.java#L21-L54)）
    - 本地 [local.properties](file://D:\Code\Project\Message-Push\Austin\austin-web\target\classes\local.properties)

---

### ✅ Redis 工具类封装

- 提供常用操作封装：
    - [mGet](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\utils\RedisUtils.java#L32-L48)、[hGetAll](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\utils\RedisUtils.java#L55-L62)、[lRange](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\utils\RedisUtils.java#L69-L76)
    - [pipelineSetEx](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\utils\RedisUtils.java#L81-L93) 设置并过期
    - [lPush](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\utils\RedisUtils.java#L99-L109) + `expire`
    - Lua 脚本执行（如限流脚本）

---

### ✅ HTTP 请求工具类

- 基于 OkHttp 封装：
    - [OkHttpUtils.doGet(...)](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\utils\OkHttpUtils.java#L32-L34)
    - [OkHttpUtils.doPost(...)](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\utils\OkHttpUtils.java#L96-L110)
    - JSON/XML 支持
    - 自定义 headers 支持

---

### ✅ 日志记录与链路追踪

- 继承 `CustomLogListener` 并重写 [createLog(LogDTO logDTO)](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\utils\LogUtils.java#L32-L35)
- 支持将日志写入 Redis 或 Kafka，用于后续分析

---

### ✅ Token 获取与缓存

- 支持钉钉、个推等第三方服务的 Token 获取
- 缓存到 Redis 并自动刷新
- [AccessTokenUtils.getAccessToken(...)](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\utils\AccessTokenUtils.java#L53-L79) 可扩展至更多平台

---

### ✅ 账号信息管理

- 支持微信小程序、公众号、短信账号的配置读取
- 支持从数据库中加载 [ChannelAccount](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\domain\ChannelAccount.java#L13-L64) 配置
- 自动初始化 WxMpService / WxMaService

---

### ✅ URL 构建与占位符替换

- [TaskInfoUtils.generateUrl(...)](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\utils\TaskInfoUtils.java#L57-L65) 添加业务标识码
- [ContentHolderUtil.replacePlaceHolder(...)](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\utils\ContentHolderUtil.java#L30-L32) 替换模板中的变量（格式：`{$var}`）

---

### ✅ 文件下载工具类

- [AustinFileUtils.getRemoteUrl2File(...)](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\utils\AustinFileUtils.java#L35-L68) 下载远程文件（支持 http/https/oss）

---

## 🧪 单元测试 & 依赖注入

- 使用 Spring Boot 的自动装配机制
- 各工具类都提供了 `@Component` 注解以供注入
- 支持多种条件注解控制 Bean 加载

---

## 🛠️ 技术细节

### 🧱 分层结构清晰

| 包名 | 功能 |
|------|------|
| [config](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\action\DeduplicationAction.java#L31-L32) | 配置类（OkHttp、线程池、RabbitMQ等） |
| `constans` | 常量类（如消息队列类型常量） |
| `dao` | JPA 数据访问层（ChannelAccount、MessageTemplate、SmsRecord） |
| `domain` | 数据库实体类 |
| `dto` | 数据传输对象（如个推 Token DTO） |
| `mq` | 消息队列相关接口与实现 |
| [pending](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\pending\AbstractLazyPending.java#L100-L107) | 延迟任务队列 |
| `service` | 配置服务接口 |
| `utils` | 工具类集合（HTTP、Redis、Token、日志等） |

---

## 🧠 总结

> `austin-support` 是整个消息推送系统的 **基础设施模块**，它封装了消息队列、线程池、配置中心、Redis、日志、Token、URL 构建等常用功能，为上层业务模块（如 austin-api、austin-stream）提供了稳定可靠的基础能力。它的设计目标是 **高内聚、低耦合、易扩展、可插拔**，使得开发者可以专注于业务逻辑而非底层组件。