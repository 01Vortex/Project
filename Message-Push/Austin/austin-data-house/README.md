`austin-data-house` 是 `Austin` 消息推送平台中的一个**数据仓库模块**，主要用于将 Kafka 中的消息消费后写入 Hive 数据仓库，以支持后续的离线分析、数据统计和埋点追踪等场景。

---

## 🧩 核心功能

### ✅ 1. **Kafka 消息实时消费**
通过 Apache Flink 实时消费 Kafka 中的消息。消息内容通常为消息推送过程中的埋点日志（如消息状态、用户行为等）。

#### 相关常量：
- [DataHouseConstant.KAFKA_TOPIC](file://D:\Code\Project\Message-Push\Austin\austin-data-house\src\main\java\com\java3y\austin\datahouse\constants\DataHouseConstant.java#L27-L30)：指定 Kafka topic。
- [DataHouseConstant.KAFKA_IP_PORT](file://D:\Code\Project\Message-Push\Austin\austin-data-house\src\main\java\com\java3y\austin\datahouse\constants\DataHouseConstant.java#L33-L36)：指定 Kafka 地址。

---

### ✅ 2. **Flink + Hive 集成写入**
使用 Flink Table API 创建 Hive Catalog，并将 Kafka 数据写入 Hive 表中，支持结构化查询与分析。

#### 核心类：
- [AustinHiveBootStrap](file://D:\Code\Project\Message-Push\Austin\austin-data-house\src\main\java\com\java3y\austin\datahouse\AustinHiveBootStrap.java#L25-L79)：主程序入口，完成以下操作：
    - 初始化 Hive Catalog；
    - 创建 Kafka Source 表；
    - 创建 Hive Sink 表；
    - 执行 SQL 将 Kafka 数据写入 Hive 表中。

---

### ✅ 3. **Hive 表定义与分区配置**
在 Hive 中创建用于存储消息轨迹的表，并设置合理的文件格式（Parquet）和写入策略。

#### Hive 表相关常量：
- [DataHouseConstant.KAFKA_SOURCE_TABLE_NAME](file://D:\Code\Project\Message-Push\Austin\austin-data-house\src\main\java\com\java3y\austin\datahouse\constants\DataHouseConstant.java#L14-L17)：Kafka Source 表名。
- [DataHouseConstant.KAFKA_SINK_TABLE_NAME](file://D:\Code\Project\Message-Push\Austin\austin-data-house\src\main\java\com\java3y\austin\datahouse\constants\DataHouseConstant.java#L20-L23)：Hive Sink 表名。
- 使用 Parquet 存储格式，支持高效的压缩与列式查询。

---

### ✅ 4. **Hive Metastore 配置**
通过 [hive-site.xml](file://D:\Code\Project\Message-Push\Austin\austin-data-house\target\classes\hive-site.xml) 配置 Hive 元数据库连接信息，确保 Flink 能够访问 Hive 的元数据服务。

#### 配置内容：
```xml
<property>
    <name>javax.jdo.option.ConnectionURL</name>
    <value>jdbc:postgresql://hive_ip:5432/metastore?createDatabaseIfNotExist=true</value>
</property>
<property>
    <name>hive.metastore.uris</name>
    <value>thrift://hive_ip:9083</value>
</property>
```

- 使用 PostgreSQL 作为 Hive Metastore 数据库。
- 连接远程 Hive Metastore 服务。

---

## 📦 包结构说明

| 目录 | 功能 |
|------|------|
| `java/com/java3y/austin/datahouse` | 主程序类 `AustinHiveBootStrap` |
| `java/com/java3y/austin/datahouse/constants` | 常量类 `DataHouseConstant`，包含 Kafka 和 Hive 表名、地址等 |
| `resources` | 配置文件 `hive-site.xml`，用于连接 Hive Metastore |

---

## 📈 应用场景

1. **消息轨迹记录**：将消息从发送到送达的整个生命周期记录下来，便于排查问题。
2. **数据分析**：基于 Hive 的数据进行用户行为分析、推送效果评估等。
3. **埋点日志收集**：统一收集各个渠道的消息状态日志，供后续处理使用。
4. **离线报表生成**：定时从 Hive 提取数据生成运营报表。

---

## 🧱 技术栈整合

| 技术 | 用途 |
|------|------|
| Apache Flink | 实时流处理引擎 |
| Kafka | 消息队列，用于传输日志 |
| Hive | 数据仓库，用于存储和查询历史数据 |
| PostgreSQL | Hive Metastore 数据库 |
| Parquet | 列式存储格式，提升查询效率 |
| Flink Hive Connector | 实现 Flink 与 Hive 的无缝集成 |

---

## 📁 总结

`austin-data-house` 模块是 Austin 平台中负责**数据采集与数仓构建**的重要组件，具有以下特点：

- 实时消费 Kafka 日志并写入 Hive；
- 支持高效的数据存储与查询；
- 基于 Flink 构建，具备良好的扩展性和性能；
- 集成 Hive Metastore，便于管理元数据；
- 适用于消息轨迹分析、运营统计等场景。

它是构建消息推送平台数据闭环的关键一环。