`austin-service-api` 是一个 **消息推送服务的对外接口模块**，主要为上层应用或外部系统提供统一的消息发送、撤回和链路追踪等核心功能的 API 定义。

---

## 🧩 模块定位

该模块是一个 **API 接口层（Interface Layer）**，并不包含具体实现逻辑，而是定义了以下内容：

- 消息服务的标准接口（如 [SendService](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\service\SendService.java#L11-L31), [RecallService](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\service\RecallService.java#L10-L22), [TraceService](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\service\TraceService.java#L12-L21)）
- 请求参数类（如 [SendRequest](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\domain\SendRequest.java#L16-L51), [BatchSendRequest](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\domain\BatchSendRequest.java#L16-L45), [MessageParam](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\domain\MessageParam.java#L16-L47)）
- 响应结果类（如 [SendResponse](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\domain\SendResponse.java#L16-L35), [TraceResponse](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\domain\TraceResponse.java#L16-L34)）
- 业务枚举（如 [BusinessCode](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\enums\BusinessCode.java#L10-L37)）

这些接口和数据结构被其他模块（如 `austin-web`, `austin-application` 等）依赖，作为调用底层消息处理逻辑的标准契约。

---

## 🔧 主要功能

### ✅ 消息发送服务 ([SendService](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\service\SendService.java#L11-L31))
```java
SendResponse send(SendRequest sendRequest);
SendResponse batchSend(BatchSendRequest batchSendRequest);
```

- 支持单条消息发送和批量消息发送。
- 可以指定接收人、消息模板ID、变量替换等信息。

### ✅ 消息撤回服务 ([RecallService](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\service\RecallService.java#L10-L22))
```java
SendResponse recall(SendRequest sendRequest);
```

- 支持根据消息ID或消息模板ID进行消息撤回操作。

### ✅ 链路追踪服务 ([TraceService](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\service\TraceService.java#L12-L21))
```java
TraceResponse traceByMessageId(String messageId);
```

- 提供基于消息ID的链路追踪能力，便于调试与监控。

---

## 📦 核心类说明

| 类名 | 功能 |
|------|------|
| [SendRequest](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\domain\SendRequest.java#L16-L51) | 封装发送/撤回请求的基本参数（code, messageTemplateId, messageParam, recallMessageIds） |
| [BatchSendRequest](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\domain\BatchSendRequest.java#L16-L45) | 批量发送请求参数，支持多个 [MessageParam](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\domain\MessageParam.java#L16-L47) |
| [MessageParam](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\domain\MessageParam.java#L16-L47) | 单个消息参数，包括接收者、变量替换、扩展参数等 |
| [SendResponse](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\domain\SendResponse.java#L16-L35) | 发送接口返回值，包含状态码、提示信息和实际发送任务列表 |
| [TraceResponse](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\domain\TraceResponse.java#L16-L34) | 链路追踪响应，用于展示消息生命周期中的埋点信息 |
| [BusinessCode](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\enums\BusinessCode.java#L10-L37) | 枚举类型，标识当前执行的业务类型（发送 or 撤回） |

---

## 📐 模块结构

```
com.java3y.austin.service.api
├── domain       // 请求/响应模型
│   ├── SendRequest.java
│   ├── BatchSendRequest.java
│   ├── MessageParam.java
│   ├── SendResponse.java
│   └── TraceResponse.java
├── enums        // 枚举定义
│   └── BusinessCode.java
└── service      // 接口定义
    ├── SendService.java
    ├── RecallService.java
    └── TraceService.java
```


---

## 🌐 使用场景

1. **微服务间通信**：其他服务通过引入此模块依赖，可调用标准接口完成消息发送或撤回。
2. **开放平台集成**：对外暴露 RESTful API 或 RPC 接口时使用，保证输入输出的一致性。
3. **日志与监控**：通过 [TraceService](file://D:\Code\Project\Message-Push\Austin\austin-service-api\src\main\java\com\java3y\austin\service\api\service\TraceService.java#L12-L21) 实现对消息流转过程的全链路跟踪。

---

## 🧱 设计理念

- **解耦**：将接口定义与实现分离，提升系统的可维护性和可扩展性。
- **标准化**：统一请求/响应格式，便于集成和测试。
- **高内聚低耦合**：每个类职责单一，结构清晰。

---

## ✅ 总结

> `austin-service-api` 是整个消息推送系统中**承上启下的关键模块**，它为外部系统提供了标准的 API 接口，屏蔽了底层复杂的业务逻辑，是构建可插拔、可扩展、易集成的消息服务平台的重要基础。