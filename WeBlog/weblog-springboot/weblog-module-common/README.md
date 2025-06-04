`weblog-module-common` 是博客系统中的公共模块，主要为其他模块（如 `weblog-module-admin`, `weblog-module-front` 等）提供基础支持和通用能力。该模块的设计目的是实现代码复用、统一异常处理、日志记录、数据库操作封装等功能。

---

## 主要功能与结构分析

### 一、核心功能分类

#### 1. **统一响应封装**
- 提供统一的返回格式类：
    - `Response<T>`：用于封装接口调用成功或失败时的响应数据。
    - `PageResponse<T>`：继承自 [Response](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\Response.java#L15-L71)，扩展了分页信息（总条数、当前页码等）。

> 文件路径：
- `src/main/java/com/quanxiaoha/weblog/common/Response.java`
- `src/main/java/com/quanxiaoha/weblog/common/PageResponse.java`

#### 2. **统一异常处理**
- 提供全局异常处理器 [GlobalExceptionHandler](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\exception\GlobalExceptionHandler.java#L25-L98)，统一捕获并处理运行时异常、业务异常、参数校验异常等。
- 支持自定义错误码和错误信息：
    - [BizException](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\exception\BizException.java#L11-L24)：业务异常封装。
    - [BaseExceptionInterface](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\exception\BaseExceptionInterface.java#L8-L14)：错误码和错误信息接口。
    - [ResponseCodeEnum](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\enums\ResponseCodeEnum.java#L12-L41)：预定义所有错误码和提示信息。

> 文件路径：
- `src/main/java/com/quanxiaoha/weblog/common/exception/GlobalExceptionHandler.java`
- `src/main/java/com/quanxiaoha/weblog/common/exception/BizException.java`
- `src/main/java/com/quanxiaoha/weblog/common/enums/ResponseCodeEnum.java`

#### 3. **AOP 日志切面**
- 使用 AOP 实现接口请求日志记录，包括：
    - 请求时间、入参、耗时、出参等信息。
    - 自动记录访客 IP 地址及归属地。
- 注解驱动方式：通过 [@ApiOperationLog](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\aspect\ApiOperationLog.java#L4-L15) 注解标记需要记录日志的方法。

> 文件路径：
- `src/main/java/com/quanxiaoha/weblog/common/aspect/ApiOperationLogAspect.java`
- `src/main/java/com/quanxiaoha/weblog/common/aspect/ApiOperationLog.java`

#### 4. **MyBatis Plus 扩展**
- 提供 MyBatis Plus 的增强功能，例如：
    - 自定义批量插入方法（使用 [InsertBatchSqlInjector](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\config\InsertBatchSqlInjector.java#L15-L25) 和 [MyBaseMapper](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\config\MyBaseMapper.java#L13-L18)）。
    - 分页插件配置（`PaginationInnerInterceptor`）。
    - 各类实体类对应的 Mapper 接口（如 [ArticleMapper](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\domain\mapper\ArticleMapper.java#L10-L18), [UserMapper](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\domain\mapper\UserMapper.java#L6-L7) 等）。

> 文件路径：
- `src/main/java/com/quanxiaoha/weblog/common/config/MybatisPlusConfig.java`
- `src/main/java/com/quanxiaoha/weblog/common/config/InsertBatchSqlInjector.java`
- `src/main/java/com/quanxiaoha/weblog/common/config/MyBaseMapper.java`

#### 5. **通用工具类**
- 提供多个工具类，涵盖以下功能：
    - **IP 归属地查询**：[AgentRegionUtils](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\utils\AgentRegionUtils.java#L21-L106) 结合 `ip2region.xdb` 查询 IP 所在地区。
    - **日期格式化**：常量类 [Constants](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\constant\Constants.java#L12-L19) 定义常用的日期格式。
    - **事件总线 EventBus**：用于发布订阅模式的消息通信（如文章阅读 PV 更新）。

> 文件路径：
- `src/main/java/com/quanxiaoha/weblog/common/utils/AgentRegionUtils.java`
- `src/main/java/com/quanxiaoha/weblog/common/constant/Constants.java`
- `src/main/java/com/quanxiaoha/weblog/common/config/EventBusConfig.java`

#### 6. **数据模型与映射器**
- 定义系统中使用的各类数据对象（DO）及其对应的 MyBatis Mapper：
    - 数据对象（DO）：如 [ArticleDO](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\domain\dos\ArticleDO.java#L11-L24), [TagDO](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\domain\dos\TagDO.java#L10-L20), [CategoryDO](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\domain\dos\CategoryDO.java#L10-L20), [UserDO](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\domain\dos\UserDO.java#L10-L21), [BlogSettingDO](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\domain\dos\BlogSettingDO.java#L10-L24) 等。
    - Mapper：如 [ArticleMapper](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\domain\mapper\ArticleMapper.java#L10-L18), [TagMapper](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\domain\mapper\TagMapper.java#L5-L6), [UserMapper](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\domain\mapper\UserMapper.java#L6-L7) 等。

> 示例文件路径：
- `src/main/java/com/quanxiaoha/weblog/common/domain/dos/ArticleDO.java`
- `src/main/java/com/quanxiaoha/weblog/common/domain/mapper/ArticleMapper.java`

#### 7. **事件总线与监听机制**
- 使用 Google Guava 的 `EventBus` 实现事件发布与订阅机制。
- 用于异步更新文章阅读量、PV 统计等操作。

> 文件路径：
- `src/main/java/com/quanxiaoha/weblog/common/eventbus/ArticleEvent.java`
- `src/main/java/com/quanxiaoha/weblog/common/eventbus/EventListener.java`
- `src/main/java/com/quanxiaoha/weblog/common/config/EventBusConfig.java`

#### 8. **Maven 依赖管理**
- 提供常用依赖，包括：
    - Spring Boot Web、Security、Validation
    - Lombok、Guava、MapStruct
    - MySQL、MyBatis Plus、P6Spy（SQL 监控）
    - ip2region（IP 地理位置查询）

> 文件路径：
- `pom.xml`

---

## 模块结构概览

```
src/main/java/
├── aspect/                 // 日志切面相关
├── config/                 // 配置类（MyBatis Plus、EventBus、批量插入等）
├── constant/               // 全局常量类（日期格式等）
├── domain/                 // 数据模型与 Mapper
│   ├── dos/                // Data Object（实体类）
│   └── mapper/             // MyBatis Mapper 接口
├── enums/                  // 枚举类（错误码、事件类型）
├── eventbus/               // 事件总线相关类
├── exception/              // 异常处理类
├── model/                  // VO 类（如 QuerySelectListRspVO）
├── utils/                  // 工具类（IP、区域查询）
└── Response.java           // 统一响应类
└── PageResponse.java       // 分页响应类
```


---

## 总结

`weblog-module-common` 是整个博客系统的基础设施模块，其主要作用是：

| 功能类别 | 描述 |
|----------|------|
| 响应封装 | 提供统一的 API 返回格式 |
| 异常处理 | 统一捕获和处理异常 |
| 日志记录 | 通过 AOP 记录接口调用日志 |
| 数据访问 | 提供 MyBatis Plus 封装及实体类 |
| 工具类 | 提供 IP 查询、日期处理等 |
| 事件机制 | 使用 EventBus 实现异步通知 |
| 依赖管理 | 聚合通用依赖，便于其他模块引用 |

它为前后台模块提供了坚实的基础支撑，确保系统具备良好的可维护性、可扩展性和一致性。