`weblog-module-admin` 模块是博客系统后台管理模块，主要负责提供博客系统的后台管理功能。以下是该模块的主要功能和结构分析：

---

### 一、核心功能

#### 1. **文章管理**
- **发布文章**：通过 [AdminArticleController.publishArticle()](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\controller\AdminArticleController.java#L27-L32) 提供接口实现文章的发布。
- **修改文章**：支持更新已发布的文章内容。
- **删除文章**：逻辑删除指定的文章。
- **查询文章详情/列表**：分页展示文章信息并支持条件筛选（如标题、时间范围等）。

> 对应类：
- [AdminArticleController](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\controller\AdminArticleController.java#L20-L60)
- [AdminArticleService](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\service\AdminArticleService.java#L7-L18)
- [AdminArticleDao](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\dao\AdminArticleDao.java#L9-L25)

#### 2. **分类管理**
- **添加分类**：新增一个文章分类。
- **删除分类**：删除指定 ID 的分类。
- **获取分类列表**：支持分页查询及条件过滤。
- **下拉框数据**：提供所有分类用于前端选择器。

> 对应类：
- [AdminCategoryController](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\controller\AdminCategoryController.java#L23-L55)
- [AdminCategoryService](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\service\AdminCategoryService.java#L11-L19)
- [AdminCategoryDao](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\dao\AdminCategoryDao.java#L6-L10)

#### 3. **标签管理**
- **添加标签**：批量添加新标签。
- **删除标签**：根据 ID 删除标签。
- **模糊搜索标签**：支持按关键字模糊匹配标签。
- **获取标签列表**：分页展示标签信息。
- **下拉框数据**：提供所有标签用于前端选择器。

> 对应类：
- [AdminTagController](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\controller\AdminTagController.java#L23-L62)
- [AdminTagService](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\service\AdminTagService.java#L11-L21)
- [AdminTagDao](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\dao\AdminTagDao.java#L8-L18)

#### 4. **用户管理**
- **修改管理员密码**：更新管理员账号的密码。
- **获取用户信息**：当前登录用户的详细信息（用户名、头像等）。

> 对应类：
- [AdminUserController](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\controller\AdminUserController.java#L22-L43)
- [AdminUserService](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\service\AdminUserService.java#L13-L15)
- [UserDetailServiceImpl](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\service\impl\UserDetailServiceImpl.java#L29-L62)

#### 5. **博客设置管理**
- **更新博客信息**：包括博客名称、作者、头像、简介、各平台主页链接等。
- **获取博客信息**：读取当前博客的基础配置信息。

> 对应类：
- [AdminBlogSettingController](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\controller\AdminBlogSettingController.java#L22-L41)
- [AdminBlogSettingService](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\service\AdminBlogSettingService.java#L9-L16)
- [BlogSettingDO](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\domain\dos\BlogSettingDO.java#L10-L24)

#### 6. **仪表盘统计**
- **文章统计数据**：展示文章总数、分类数、标签数、总浏览量（PV）。
- **文章发布趋势**：按时间维度统计文章发布数量。
- **PV 趋势**：显示最近一周 PV 变化情况。

> 对应类：
- [AdminDashboardController](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\controller\AdminDashboardController.java#L20-L45)
- [AdminDashboardService](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\service\AdminDashboardService.java#L5-L12)
- `StatisticsArticlePVDao`

#### 7. **文件上传**
- 支持通过 MinIO 实现图片或文件的上传，并返回访问 URL。

> 对应类：
- [AdminFileController](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\controller\AdminFileController.java#L19-L32)
- [AdminFileService](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\service\AdminFileService.java#L5-L7)
- [MinioUtil](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\utils\MinioUtil.java#L23-L59)

#### 8. **异步任务处理**
- **文章阅读量异步更新**：使用 `@Async` 异步更新文章阅读次数和每日 PV 统计。

> 对应类：
- [PVIncreaseAsyncTask](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\async\PVIncreaseAsyncTask.java#L17-L35)

---

### 二、安全与权限控制
- 使用 Spring Security 配置认证和授权。
- 所有管理接口默认需要认证后才能访问，部分操作需要特定角色（如 `ROLE_ADMIN`）。

> 对应类：
- [WebSecurityConfig](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\config\WebSecurityConfig.java#L23-L74)
- [PasswordEncoderConfig](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\config\PasswordEncoderConfig.java#L13-L25)
- [UserDetailServiceImpl](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\service\impl\UserDetailServiceImpl.java#L29-L62)

---

### 三、工具与配置类
- **MinIO 文件存储**：封装了文件上传至 MinIO 的工具类。
- **线程池配置**：提供统一的异步任务执行线程池。
- **日志切面**：记录接口调用日志。
- **事件监听机制**：使用 EventBus 处理文章阅读事件。

> 对应类：
- [MinioUtil](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\utils\MinioUtil.java#L23-L59), [MinioConfig](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\config\MinioConfig.java#L16-L28), [MinioProperties](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\config\MinioProperties.java#L13-L21)
- [ThreadPoolConfig](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\config\ThreadPoolConfig.java#L12-L25)
- [ApiOperationLog](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog\common\aspect\ApiOperationLog.java#L4-L15), [AdminEventListener](file://D:\Code\Project\WeBlog\weblog-springboot\weblog-module-admin\src\main\java\com\quanxiaoha\weblog\admin\eventbus\AdminEventListener.java#L16-L32)

---

### 四、目录结构简要说明

```
src/main/java/
├── async/                  // 异步任务相关
├── config/                 // 配置类（Spring Security、MinIO、PasswordEncoder 等）
├── controller/             // 后台 REST 接口定义
├── dao/                    // 数据库操作接口（MyBatis Plus）
├── enums/                  // 枚举类（响应码等）
├── eventbus/               // 事件监听相关
├── model/                  // VO、DTO 等模型定义
│   └── vo/                 // 各种请求和响应 VO
├── service/                // 业务服务层接口及实现
├── utils/                  // 工具类（如 MinIO 上传）
└── package-info.java       // 包描述
```


---

### 总结

`weblog-module-admin` 是一个典型的 Spring Boot 后台管理模块，涵盖了博客系统的核心管理功能，包括文章、分类、标签、用户、博客设置、统计仪表盘等，具备良好的模块划分、权限控制、异步处理和文件上传能力，适用于中后台管理系统开发场景。