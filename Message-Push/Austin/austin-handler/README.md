`austin-handler` 是一个 Java 模块，主要负责消息推送系统的**消息处理流程**。该模块作为整个系统中核心的业务逻辑处理部分，实现了从接收消息、校验、过滤、限流、路由到发送等完整的处理链路。

### 主要功能和作用如下：

1. **消息处理流水线（Pipeline）**
    - 使用 [BusinessProcess](file://D:\Code\Project\Message-Push\Austin\austin-common\src\main\java\com\java3y\austin\common\pipeline\BusinessProcess.java#L7-L15) 接口定义消息处理流程中的各个阶段。
    - 通过实现该接口的类如：
        - [ShieldAction](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\action\ShieldAction.java#L27-L67)：夜间屏蔽控制
        - [SensWordsAction](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\action\SensWordsAction.java#L20-L185)：敏感词过滤
        - [DeduplicationAction](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\action\DeduplicationAction.java#L26-L57)：消息去重
        - [SendMessageAction](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\action\SendMessageAction.java#L17-L39)：路由并发送消息
    - 支持插件式结构，方便扩展新的处理步骤。

2. **消息渠道支持**
    - 支持多种消息通道（Channel），例如：
        - 微信小程序、公众号
        - 支付宝小程序
        - 短信（SMS）
        - 钉钉、飞书等
    - 每个渠道都有对应的 [Handler](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\handler\Handler.java#L9-L26) 实现类，继承自 [BaseHandler](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\handler\BaseHandler.java#L19-L87)，统一管理发送逻辑。
    - 示例类：
        - [AlipayMiniProgramParam](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\domain\alipay\AlipayMiniProgramParam.java#L12-L42)：支付宝小程序参数封装
        - `QrCodeService`：微信二维码生成服务

3. **限流与负载均衡**
    - 提供限流策略支持，使用 [FlowControlFactory](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\flowcontrol\FlowControlFactory.java#L29-L100) 和 [FlowControlService](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\flowcontrol\FlowControlService.java#L8-L20) 实现流量控制。
    - 支持滑动窗口、简单计数等多种限流算法。
    - 负载均衡支持，通过 [ServiceLoadBalancerFactory](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\loadbalance\ServiceLoadBalancerFactory.java#L20-L54) 实现多实例选择策略。

4. **消息去重机制**
    - 支持基于内容的 N 分钟内重复消息去重（[SlideWindowLimitService](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\deduplication\limit\SlideWindowLimitService.java#L25-L72)）
    - 支持同一用户一天内频次限制（[SimpleLimitService](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\deduplication\limit\SimpleLimitService.java#L21-L78)）
    - 去重规则可配置，通过 [ConfigService](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\service\ConfigService.java#L8-L21) 获取配置。

5. **敏感词过滤**
    - 敏感词加载、缓存到 Redis，并定时更新。
    - 支持热更新机制，保证实时性。
    - 相关类：[SensitiveWordsConfig](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\config\SensitiveWordsConfig.java#L31-L155)、[SensWordsAction](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\action\SensWordsAction.java#L20-L185)

6. **消息屏蔽逻辑**
    - 夜间消息屏蔽或延迟发送功能。
    - 利用 Redis 缓存延迟任务，结合定时任务框架实现。

7. **线程池管理**
    - 使用 [HandlerThreadPoolConfig](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\config\HandlerThreadPoolConfig.java#L15-L44) 统一管理线程池资源。
    - 支持动态调整线程池大小、队列类型等。

8. **消息撤回与状态跟踪**
    - 提供消息撤回功能，将撤回信息记录至 Redis。
    - 日志追踪使用 [LogUtils](file://D:\Code\Project\Message-Push\Austin\austin-support\src\main\java\com\java3y\austin\support\utils\LogUtils.java#L19-L68) 记录关键节点状态（如发送成功/失败、屏蔽等）。

9. **工具类与辅助功能**
    - [GroupIdMappingUtils](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\utils\GroupIdMappingUtils.java#L16-L49)：用于标识不同渠道和消息类型的消费者组 ID。
    - [AlipayClientSingleton](file://D:\Code\Project\Message-Push\Austin\austin-handler\src\main\java\com\java3y\austin\handler\config\AlipayClientSingleton.java#L17-L43)：支付宝客户端单例管理。
    - `QrCodeService`：微信二维码生成服务。

---

### 总结
该模块是一个高可扩展、低耦合的消息处理引擎，具备完整的消息处理生命周期管理能力，适用于多通道、高频次、需控制节奏的场景，是整个消息推送系统的核心中枢。