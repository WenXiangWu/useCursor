# 德州扑克游戏

这是一个使用Java开发的多人德州扑克游戏，具有现代化的界面和实时对战功能。

## 项目结构

```
poker-game/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── poker/
│   │   │           ├── config/        # 配置类
│   │   │           ├── controller/    # 控制器
│   │   │           ├── model/         # 数据模型
│   │   │           ├── service/       # 业务逻辑
│   │   │           ├── ui/            # JavaFX UI组件
│   │   │           └── util/          # 工具类
│   │   └── resources/
│   │       ├── static/               # 静态资源
│   │       ├── templates/            # FXML模板
│   │       └── application.properties # 配置文件
│   └── test/                         # 测试代码
└── pom.xml                           # Maven配置文件
```

## 技术栈

- 后端：Spring Boot, WebSocket, Spring Security
- 前端：JavaFX, CSS
- 数据库：MySQL
- 构建工具：Maven

## 功能特性

- 多人实时对战
- 现代化的用户界面
- 用户认证和授权
- 游戏历史记录
- 排行榜系统

## 开发环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+

## 如何运行

1. 克隆项目
2. 配置数据库连接
3. 运行 `mvn spring-boot:run`
4. 访问 `http://localhost:8080`

## 开发指南

本项目使用 Cursor IDE 进行开发，充分利用了其高级特性：

- 智能代码补全
- 实时错误检测
- 代码重构工具
- 集成调试器
- Git 版本控制
