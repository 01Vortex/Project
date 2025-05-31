`austin-cron` 是 `Austin` 消息推送平台的一个模块，主要用于实现**定时任务调度、异步处理和消息延迟分发**等功能。该模块基于 **XXL-JOB** 调度框架进行集成和封装，结合线程池、文件读取、Redis 缓存等技术，实现了多种后台任务的自动化执行与管理。

---

## 🧩 功能概述

### ✅ 1. 定时任务调度（XXL-JOB 集成）
通过集成 XXL-JOB 实现对消息模板的定时触发，主要功能包括：

- **创建/更新定时任务**
- **删除定时任务**
- -
- **查询执行器 ID**
- **创建执行器组**

#### 核心类：
- [XxlJobConfig](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\xxl\config\XxlJobConfig.java#L13-L51)：配置 XXL-JOB 执行器。
- [CronTaskService](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\xxl\service\CronTaskService.java#L11-L65) / [CronTaskServiceImpl](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\xxl\service\impl\CronTaskServiceImpl.java#L32-L239)：封装定时任务的增删改查接口。
- [XxlJobUtils](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\xxl\utils\XxlJobUtils.java#L25-L92)：构建定时任务参数、获取执行器信息。
- [XxlJobInfo](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\xxl\entity\XxlJobInfo.java#L17-L124) / [XxlJobGroup](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\xxl\entity\XxlJobGroup.java#L16-L51)：表示任务信息和执行器组信息。
- [XxlJobConstant](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\xxl\constants\XxlJobConstant.java#L8-L47)：常量定义，如 URL、Cookie 前缀等。
- `XxlJobEnums`：各种枚举类型（调度策略、失败策略等）。

---

### ✅ 2. 异步线程池管理
使用 Hutool 和 DTP（Dynamic Thread Pool）实现灵活的线程池配置，支持动态调整参数。

#### 核心类：
- [CronAsyncThreadPoolConfig](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\config\CronAsyncThreadPoolConfig.java#L18-L67)：定义多个线程池，用于接收 XXL-JOB 请求、消费队列等。

---

### ✅ 3. 文件处理与人群批量发送
支持从 CSV 文件中读取用户群体数据，并按批次异步发送消息。

#### 核心类：
- [ReadFileUtils](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\utils\ReadFileUtils.java#L23-L127)：CSV 文件读取工具。
- [CountFileRowHandler](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\csv\CountFileRowHandler.java#L11-L24)：统计 CSV 行数。
- [CrowdBatchTaskPending](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\pending\CrowdBatchTaskPending.java#L34-L84)：实现延迟批量发送逻辑。
- [CrowdInfoVo](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\vo\CrowdInfoVo.java#L17-L38)：每行 CSV 数据的封装对象。
- [TaskHandler](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\service\TaskHandler.java#L7-L16) / [TaskHandlerImpl](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\service\impl\TaskHandlerImpl.java#L25-L85)：处理定时任务触发后的具体逻辑，如读取文件、分批发送。

---

### ✅ 4. 夜间屏蔽消息延迟发送
针对夜间收到的消息，支持延迟到次日早上9点发送。

#### 核心类：
- [NightShieldLazyPendingHandler](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\handler\NightShieldLazyPendingHandler.java#L26-L59)：监听并处理夜间屏蔽队列中的消息，延后发送至 Kafka。

---

### ✅ 5. 第三方 Token 自动刷新
自动刷新钉钉、个推等第三方平台的访问令牌，确保调用接口时 token 有效。

#### 核心类：
- [RefreshDingDingAccessTokenHandler](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\handler\RefreshDingDingAccessTokenHandler.java#L27-L59)：刷新钉钉 access_token。
- [RefreshGeTuiAccessTokenHandler](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\handler\RefreshGeTuiAccessTokenHandler.java#L27-L60)：刷新个推 token。
- 使用 Redis 缓存 token，避免重复请求。

---

### ✅ 6. Cookie 管理与登录认证
封装对 XXL-JOB 登录、Cookie 获取与缓存的逻辑。

#### 核心类：
- [CronTaskServiceImpl](file://D:\Code\Project\Message-Push\Austin\austin-cron\src\main\java\com\java3y\austin\cron\xxl\service\impl\CronTaskServiceImpl.java#L32-L239) 中封装了登录逻辑，使用 Redis 存储 Cookie。

---

## 📦 包结构详解

| 目录 | 功能 |
|------|------|
| [config](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\config\WeChatLoginConfig.java#L47-L47) | 线程池、XXL-JOB 执行器配置 |
| `constants` | 各种常量定义 |
| [csv](file://D:\Code\Project\Message-Push\Austin\austin-web\target\classes\example.csv) | CSV 文件处理相关类 |
| `dto` | 数据传输对象，如 Token 请求、响应 DTO |
| [handler](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\handler\BaseHandler.java#L68-L68) | XXL-JOB 的任务处理器，如定时任务、Token 刷新等 |
| [pending](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\pending\AbstractLazyPending.java#L100-L107) | 延迟任务队列处理类 |
| `service` | 定时任务服务接口及其实现 |
| `utils` | 工具类，如文件读取、Redis 操作等 |
| `vo` | 视图对象，用于封装业务数据 |
| `xxl` | XXL-JOB 相关实体、枚举、工具类 |

---

## 📈 应用场景

1. **定时消息推送**：如每日早报、提醒类通知。
2. **大批量用户消息下发**：支持从 CSV 文件导入人群，异步分批发送。
3. **夜间屏蔽机制**：凌晨收到的消息可延迟到次日发送。
4. **第三方系统 Token 管理**：自动刷新钉钉、个推等系统的访问 token。
5. **任务调度可视化**：通过 XXL-JOB 管理界面查看、启动、停止任务。

---

## 🧱 技术栈整合

| 技术 | 用途 |
|------|------|
| XXL-JOB | 分布式任务调度框架 |
| Spring Boot | 快速搭建微服务 |
| Hutool | 工具类库（文件读取、HTTP 请求等） |
| Lombok | 简化 Java Bean 写法 |
| Redis | 缓存 token、cookie、任务状态 |
| Kafka | 异步消息队列，用于夜间屏蔽任务延迟发送 |
| FastJSON | JSON 序列化与反序列化 |
| Dynamic TP (DTP) | 动态线程池管理 |

---

## 📁 总结

`austin-cron` 模块是整个 Austin 平台中负责**定时任务调度、异步处理、延迟消息发送、第三方 token 刷新**的核心模块，具有以下特点：

- 支持高并发、低延迟的任务处理；
- 提供灵活的线程池配置；
- 支持从文件导入人群并批量发送；
- 与 XXL-JOB 深度集成，便于管理和监控；
- 支持夜间屏蔽策略；
- 可扩展性强，适用于各类定时或延迟任务场景。

它是构建企业级消息推送平台的重要支撑模块之一。