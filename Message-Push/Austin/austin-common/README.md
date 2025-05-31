# austin-common
`austin-common` 包是消息推送项目 `Austin` 中的**公共模块**，主要提供通用功能供其他模块依赖使用。根据其目录结构和代码内容，该包的功能可以归纳如下：

---

### ✅ 1. **常量定义**
提供系统中广泛使用的常量信息：
- **AustinConstant**：业务相关常量（如 [businessId](file://D:\Code\Project\Message-Push\Austin\austin-web\src\main\java\com\java3y\austin\web\vo\DataParam.java#L36-L36) 长度、最大接收人数、默认创建者等）。
- **CommonConstant**：通用常量（如符号字符、编码格式、HTTP协议、请求方法等）。
- **OfficialAccountParamConstant**：微信服务号参数常量（如接口参数名、处理器名称等）。
- **SendChanelUrlConstant**：各消息渠道的 API 地址（如个推、钉钉、企业微信、支付宝小程序等）。
- **ThreadPoolConstant**：线程池配置常量（核心线程数、队列大小等）。

---

### ✅ 2. **领域模型（Domain）**
定义系统中关键数据结构：
- **AnchorInfo**：埋点信息，用于链路追踪和日志记录。
- **LogParam**：日志参数封装。
- **RecallTaskInfo**：撤回任务信息。
- **SimpleAnchorInfo / SimpleTaskInfo**：简化版埋点与任务信息。
- **TaskInfo**：发送任务信息，包含接收者、消息类型、内容模型等。

---

### ✅ 3. **DTO（Data Transfer Object）**
用于封装传输数据对象，包括：
#### 📌 账户信息 DTO
- **DingDingRobotAccount**：钉钉机器人账号。
- **DingDingWorkNoticeAccount**：钉钉工作通知账号。
- **EnterpriseWeChatRobotAccount**：企业微信机器人账号。
- **FeiShuRobotAccount**：飞书机器人账号。
- **GeTuiAccount**：个推账号。
- **WeChatMiniProgramAccount / WeChatOfficialAccount**：微信小程序/服务号账号。
- **AlipayMiniProgramAccount**：支付宝小程序账号。
- **SmsAccount 及其子类（如 TencentSmsAccount, YunPianSmsAccount）**：短信服务商账号。

#### 📌 消息内容模型 DTO
- **ContentModel 及其子类（如 SmsContentModel, EmailContentModel, DingDingRobotContentModel 等）**：不同渠道的消息内容结构。

---

### ✅ 4. **枚举类（Enums）**
定义系统中各类状态码、类型码、错误码等：
- **AnchorState**：埋点状态。
- **AuditStatus**：审核状态。
- **ChannelType**：消息发送渠道类型（如短信、邮件、微信、钉钉等）。
- **DeduplicationType**：去重类型。
- **MessageType**：消息类型（如通知、营销、验证码）。
- **MessageStatus**：消息发送状态。
- **ShieldType**：屏蔽策略。
- **TemplateType**：模板类型（定时、实时）。
- **RespStatusEnum**：全局响应状态码。
- **IdType**：接收者 ID 类型（用户ID、手机号、OpenID等）。
- **SendMessageType**：支持的消息内容类型（文本、图片、链接、Markdown等）。
- **FileType**：文件类型（图片、语音、视频等）。
- **SmsSupplier**：短信服务商。

---

### ✅ 5. **流程处理框架（Pipeline）**
实现责任链模式的基础组件：
- **BusinessProcess**：业务执行器接口。
- **ProcessContext**：流程上下文，携带执行过程中的数据。
- **ProcessController**：流程控制器，调度多个处理器。
- **ProcessException**：流程异常封装。
- **ProcessModel**：流程数据模型接口。
- **ProcessTemplate**：流程模板，配置处理器列表。

---

### ✅ 6. **工具类与辅助类**
- **BasicResultVO**：统一返回结果封装类，用于 REST 接口返回值。
- **EnumUtil**：枚举工具类，用于获取描述、code 列表等。
- **PowerfulEnum**：增强型枚举接口，定义通用行为。

---

### ✅ 7. **Maven 配置**
- [pom.xml](file://D:\Code\Project\Message-Push\Austin\pom.xml)：声明公共依赖（如 Lombok），并继承自父项目 `austin`。

---

### 🧩 总结

| 功能分类       | 内容                                                                 |
|----------------|----------------------------------------------------------------------|
| 常量管理       | 各种系统级和业务级常量                                               |
| 数据模型       | 领域模型（Domain）、传输对象（DTO）、消息内容模型                   |
| 枚举管理       | 各类状态码、类型码、错误码                                         |
| 流程控制       | 基于责任链模式的流程引擎                                           |
| 工具类         | 统一返回值封装、枚举工具等                                         |
| 第三方集成支持 | 封装钉钉、微信、个推、短信服务商等所需账户和消息内容结构             |

---

### 📦 使用场景
`austin-common` 是整个 `Austin` 项目的基础支撑模块，被其他模块（如 austin-support、austin-application）依赖，主要用于：
- 定义统一的数据结构和规范；
- 提供可复用的逻辑处理组件；
- 支持多种消息通道的扩展；
- 实现跨模块通信和统一返回机制。

适用于构建可插拔、可扩展、高内聚低耦合的消息推送平台。