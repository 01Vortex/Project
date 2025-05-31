`austin-stream` 是一个基于 **Apache Flink** 的实时流处理模块，主要负责从 Kafka 中消费消息，并对消息进行解析、转换和写入 Redis 等下游系统的操作。它是整个消息推送系统中的 **数据流处理引擎（Stream Processing Engine）**。

---

## 🧩 模块定位

- **作用**：消费 Kafka 消息，将链路埋点信息写入 Redis。
- **用途**：支持消息的链路追踪与统计分析。
- **技术栈**：
    - Apache Flink
    - Apache Kafka
    - Redis (Lettuce 客户端)
    - Spring Boot 工具类（无 Spring 上下文）

---

## 📦 核心功能

### ✅ Kafka 消息消费

使用 `KafkaSource` 从 Kafka Topic (`austinTraceLog`) 拉取消息：

```java
KafkaSource<String> kafkaConsumer = MessageQueueUtils.getKafkaConsumer(AustinFlinkConstant.TOPIC_NAME, AustinFlinkConstant.GROUP_ID, AustinFlinkConstant.BROKER);
DataStreamSource<String> kafkaSource = env.fromSource(kafkaConsumer, WatermarkStrategy.noWatermarks(), AustinFlinkConstant.SOURCE_NAME);
```


- 使用 `SimpleStringSchema` 反序列化消息内容为字符串。
- 从最早的 offset 开始消费：`.setStartingOffsets(OffsetsInitializer.earliest())`

---

### ✅ 数据格式转换

通过 `AustinFlatMapFunction` 将 Kafka 中的 JSON 字符串反序列化为 `AnchorInfo` 对象：

```java
SingleOutputStreamOperator<AnchorInfo> dataStream = kafkaSource.flatMap(new AustinFlatMapFunction()).name(AustinFlinkConstant.FUNCTION_NAME);
```


- 使用 `fastjson` 解析 JSON 字符串为对象。
- 实现 `FlatMapFunction<String, AnchorInfo>` 接口。

---

### ✅ 实时数据写入 Redis

使用自定义 Sink 类 `AustinSink`，将 `AnchorInfo` 写入 Redis，实现多维度的数据统计和查询：

#### 🔹 按 `messageId` 存储链路详情

```java
redisAsyncCommands.lpush(redisMessageKey.getBytes(...), JSON.toJSONString(messageAnchorInfo).getBytes(...));
redisAsyncCommands.expire(...);
```


- key: `Austin:MessageId:{messageId}`
- value: list of state logs (`SimpleAnchorInfo`)
- 过期时间：3天

#### 🔹 按 `userId` 存储用户收到的消息链路

```java
for (String id : info.getIds()) {
    redisAsyncCommands.lpush(id.getBytes(...), JSON.toJSONString(userAnchorInfo).getBytes(...));
    redisAsyncCommands.expire(...);
}
```


- key: `{userId}`
- value: list of message logs
- 过期时间：当天结束前

#### 🔹 按 `businessId` 统计状态数量

```java
redisAsyncCommands.hincrby(String.valueOf(info.getBusinessId()).getBytes(...),
        String.valueOf(info.getState()).getBytes(...), info.getIds().size());
```


- key: `{businessId}`
- value: hash of state counts
- 过期时间：30天

---

## ⚙️ 核心类说明

| 类名 | 功能 |
|------|------|
| [AustinBootStrap](file://D:\Code\Project\Message-Push\Austin\austin-stream\src\main\java\com\java3y\austin\stream\AustinBootStrap.java#L11-L46) | Flink 主启动类，构建流处理流程 |
| [AustinFlatMapFunction](file://D:\Code\Project\Message-Push\Austin\austin-stream\src\main\java\com\java3y\austin\stream\function\AustinFlatMapFunction.java#L11-L20) | 将 Kafka 消息从 JSON 字符串转为 [AnchorInfo](file://D:\Code\Project\Message-Push\Austin\austin-common\src\main\java\com\java3y\austin\common\domain\AnchorInfo.java#L14-L54) 对象 |
| [AustinSink](file://D:\Code\Project\Message-Push\Austin\austin-stream\src\main\java\com\java3y\austin\stream\sink\AustinSink.java#L11-L83) | 自定义 Sink，将 [AnchorInfo](file://D:\Code\Project\Message-Push\Austin\austin-common\src\main\java\com\java3y\austin\common\domain\AnchorInfo.java#L14-L54) 写入 Redis |
| [RedisPipelineCallBack](file://D:\Code\Project\Message-Push\Austin\austin-stream\src\main\java\com\java3y\austin\stream\callback\RedisPipelineCallBack.java#L11-L23) | Redis Pipeline 回调接口 |
| [LettuceRedisUtils](file://D:\Code\Project\Message-Push\Austin\austin-stream\src\main\java\com\java3y\austin\stream\utils\LettuceRedisUtils.java#L11-L55) | 基于 Lettuce 的 Redis 工具类，支持 pipeline 批量操作 |
| [MessageQueueUtils](file://D:\Code\Project\Message-Push\Austin\austin-stream\src\main\java\com\java3y\austin\stream\utils\MessageQueueUtils.java#L11-L34) | Kafka 工具类，创建 Kafka Source |
| [AustinFlinkConstant](file://D:\Code\Project\Message-Push\Austin\austin-stream\src\main\java\com\java3y\austin\stream\constants\AustinFlinkConstant.java#L11-L37) | 常量配置类，包含 Kafka 和 Redis 配置信息 |

---

## 🛠️ 技术细节

### 🐳 Kafka Consumer 配置

- Broker: `austin-kafka:9092`
- Group ID: `austinLogGroup`
- Topic: `austinTraceLog`

### 🧲 Redis 配置

- Host: `austin-redis`
- Port: `6379`
- Password: `austin`

### ⏱️ Redis Key 过期策略

| Key 类型 | 过期时间 |
|----------|----------|
| `Austin:MessageId:{messageId}` | 3天 |
| `{userId}` | 当天剩余时间 |
| `{businessId}` | 30天 |

---

## 🧪 启动方式

该模块是一个独立的 Flink 应用程序，可通过以下方式部署运行：

```bash
flink run -c com.java3y.austin.stream.AustinBootStrap austin-stream.jar
```


或直接在 IDE 中运行 [AustinBootStrap.main()](file://D:\Code\Project\Message-Push\Austin\austin-stream\src\main\java\com\java3y\austin\stream\AustinBootStrap.java#L22-L43) 方法。

---

## 📐 设计理念

- **轻量级设计**：不依赖 Spring 上下文，仅使用工具类完成核心逻辑。
- **高吞吐写入**：使用 Redis Pipeline 提升写入性能。
- **可扩展性强**：未来可接入 Hive 或其他存储用于离线分析。
- **结构清晰**：分层明确，职责单一，便于维护与测试。

---

## 🧠 总结

> `austin-stream` 是整个消息推送系统的 **实时流处理模块**，其核心作用是从 Kafka 中拉取链路埋点数据，经过 Flink 处理后，写入 Redis 供链路追踪服务查询。它实现了消息的实时统计与分析能力，是系统可观测性的重要组成部分。