# VoiceLink Guardian 深联守护

## 项目介绍

VoiceLink Guardian（深联守护）是一个基于前后端分离架构的语音交互训练平台，旨在通过游戏化的方式帮助用户提升语音交互能力。平台包含多个关卡设计，用户可以通过完成关卡任务来提高自己的语音交互技能。

## 技术栈

### 后端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.6.13 | 后端框架 |
| MyBatis Plus | 3.5.14 | ORM 框架 |
| MySQL | 8.0+ | 数据库 |
| Hutool AI | 5.8.38 | AI 工具库 |
| Knife4j | 4.4.0 | API 文档 |
| Lombok | 1.18.24 | 代码简化工具 |
| Java | 1.8 | 开发语言 |

### 前端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.4.21 | 前端框架 |
| Vue Router | 4.3.0 | 路由管理 |
| Axios | 1.6.7 | HTTP 客户端 |
| Chart.js | 4.5.1 | 图表库 |
| Vite | 5.1.6 | 构建工具 |

## 项目结构

```
voiceLink-guardian/
├── voiceLink-guardian-backend/  # 后端代码
│   ├── sql/                     # 数据库脚本
│   │   └── create_table.sql     # 表创建脚本
│   ├── src/                     # 源代码
│   │   ├── main/                # 主代码
│   │   │   ├── java/            # Java 代码
│   │   │   └── resources/       # 资源文件
│   │   └── test/                # 测试代码
│   └── pom.xml                  # Maven 配置
└── voiceLink-guardian-fronted/  # 前端代码
    ├── src/                     # 源代码
    │   ├── api/                 # API 调用
    │   ├── assets/              # 静态资源
    │   ├── router/              # 路由配置
    │   ├── utils/               # 工具函数
    │   ├── views/               # 页面组件
    │   ├── App.vue              # 根组件
    │   └── main.js              # 入口文件
    ├── .env.development         # 开发环境配置
    ├── package.json             # npm 配置
    └── vite.config.js           # Vite 配置
```

## 核心功能模块

### 1. 用户管理

- 用户注册
- 用户登录
- 用户信息管理
- 角色权限控制

### 2. 游戏关卡系统

- 第一关：基础语音识别
- 第二关：语音指令执行
- 第三关：多轮对话
- 第四关：情感识别
- 第五关：复杂场景处理
- 第六关：综合能力测试

### 3. 设计中心

- 关卡设计
- 题目管理
- 难度调整

### 4. 进度跟踪

- 用户关卡进度
- 成绩统计
- 学习报告

## 快速开始

### 环境要求

- Java 1.8+
- MySQL 8.0+
- Node.js 16+
- npm 8+

### 后端部署

1. **克隆项目**

   ```bash
   git clone https://github.com/qiuping01/voiceLink-guardian.git
   cd voiceLink-guardian/voiceLink-guardian-backend
   ```

2. **创建数据库**

   ```sql
   CREATE DATABASE shenglian_guardian CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **执行数据库脚本**

   ```bash
   mysql -u root -p shenglian_guardian < sql/create_table.sql
   ```

4. **修改配置文件**

   编辑 `src/main/resources/application.yml`，修改数据库连接信息：

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/shenglian_guardian
       username: root
       password: 123456
   ```

5. **启动项目**

   ```bash
   mvn spring-boot:run
   ```

   后端服务将在 `http://localhost:8848/api` 启动。

6. **访问接口文档**

   启动成功后，访问 `http://localhost:8848/api/doc.html` 查看 API 文档。

### 前端部署

1. **进入前端目录**

   ```bash
   cd voiceLink-guardian/voiceLink-guardian-fronted
   ```

2. **安装依赖**

   ```bash
   npm install
   ```

3. **修改环境配置**

   编辑 `.env.development`，确保 API 地址正确：

   ```
   VITE_API_BASE_URL=http://localhost:8848/api
   ```

4. **启动开发服务器**

   ```bash
   npm run dev
   ```

   前端服务将在 `http://localhost:5173` 启动。

5. **构建生产版本**

   ```bash
   npm run build
   ```

   构建产物将输出到 `dist` 目录。

## 数据库设计

### 用户表（user）

| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | BIGINT | 用户ID |
| groupName | VARCHAR | 组名 |
| password | VARCHAR | 密码 |
| userRole | VARCHAR | 用户角色 |
| createTime | DATETIME | 创建时间 |
| updateTime | DATETIME | 更新时间 |
| isDelete | INT | 是否删除 |

### 设计中心表（design_center）

| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | BIGINT | 设计ID |
| name | VARCHAR | 设计名称 |
| description | TEXT | 设计描述 |
| content | TEXT | 设计内容 |
| createTime | DATETIME | 创建时间 |
| updateTime | DATETIME | 更新时间 |
| isDelete | INT | 是否删除 |

### 关卡答案表（level_answer）

| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | BIGINT | 答案ID |
| level | INT | 关卡级别 |
| question | TEXT | 问题 |
| answer | TEXT | 答案 |
| createTime | DATETIME | 创建时间 |
| updateTime | DATETIME | 更新时间 |
| isDelete | INT | 是否删除 |

### 用户进度表（user_progress）

| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | BIGINT | 进度ID |
| userId | BIGINT | 用户ID |
| level | INT | 关卡级别 |
| progress | INT | 进度值 |
| score | INT | 得分 |
| createTime | DATETIME | 创建时间 |
| updateTime | DATETIME | 更新时间 |
| isDelete | INT | 是否删除 |

## API 接口

### 用户相关

- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `GET /api/user/info` - 获取用户信息

### 关卡相关

- `GET /api/level/list` - 获取关卡列表
- `GET /api/level/detail` - 获取关卡详情
- `POST /api/level/submit` - 提交关卡答案
- `GET /api/level/progress` - 获取关卡进度

### 设计中心相关

- `GET /api/design/list` - 获取设计列表
- `POST /api/design/create` - 创建设计
- `POST /api/design/update` - 更新设计
- `POST /api/design/delete` - 删除设计

## 技术亮点

1. **前后端分离架构**：采用 Vue 3 + Spring Boot 实现前后端分离，提高开发效率和系统可维护性。

2. **游戏化学习体验**：将语音交互训练设计为游戏关卡形式，提高用户学习兴趣和参与度。

3. **AI 能力集成**：使用 Hutool AI 工具库集成语音识别、情感分析等 AI 能力。

4. **完善的接口文档**：使用 Knife4j 生成详细的 API 文档，方便前后端协作开发。

5. **响应式设计**：前端采用 Vue 3 实现响应式设计，适配不同设备屏幕。

6. **安全机制**：实现了用户认证、权限控制等安全机制，保障系统安全。

## 开发规范

### 后端开发规范

1. 遵循 Spring Boot 最佳实践
2. 使用 MyBatis Plus 进行数据库操作
3. 采用 RESTful API 设计风格
4. 统一异常处理和响应格式
5. 代码注释完整，遵循 Java 编码规范

### 前端开发规范

1. 遵循 Vue 3 组合式 API 最佳实践
2. 组件化开发，提高代码复用性
3. 使用 ESLint 进行代码检查
4. 遵循 CSS 命名规范
5. 代码注释完整，遵循 JavaScript 编码规范

## 贡献指南

1. Fork 本仓库
2. 创建特性分支：`git checkout -b feature/xxx`
3. 提交代码：`git commit -m 'Add some feature'`
4. 推送到分支：`git push origin feature/xxx`
5. 提交 Pull Request

## 许可证

Apache License 2.0

## 联系方式

- 项目地址：https://github.com/qiuping01/voiceLink-guardian
- 联系人：Qiu Ping
- 邮箱：16608683257@163.com

## 开发日志

### v1.0.0 (2025-11-25)

- 最终版本开发完成
- 实现用户注册、登录功能
- 实现6个游戏关卡
- 实现设计中心功能
- 实现进度跟踪功能