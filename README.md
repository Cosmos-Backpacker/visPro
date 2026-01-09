# visPro - Spring Cloud 微服务项目

## 项目概述

visPro 是一个基于 Spring Cloud 构建的综合性微服务应用程序。该项目提供多种服务，包括身份验证、系统管理以及集成AI功能的图像处理能力。

## 架构

该项目采用微服务架构，主要组件包括：

- **visPro-gateway**: API 网关，将请求路由到适当的服务
- **visPro-auth**: 身份验证和授权服务
- **visPro-system**: 主系统服务，包含业务逻辑和AI功能
- **visPro-common**: 共享工具和通用组件
- **visPro-api**: 服务间通信接口

## 先决条件

在运行项目之前，请确保已安装以下软件：

- Java 17 或更高版本
- Maven 3.6.0 或更高版本
- Docker 和 Docker Compose（用于容器化部署）
- MySQL 数据库（用于存储用户数据和对话历史）
- Redis（用于缓存和令牌管理）

## 快速开始

### 方法 1: 使用 Maven 运行（开发模式）

1. **克隆仓库**
   ```bash
   git clone <repository-url>
   cd visPro
   ```

2. **构建项目**
   ```bash
   mvn clean install
   ```

3. **按正确顺序启动服务**
   
   首先确保数据库和 Redis 正在运行，然后：
   
   ```bash
   # 终端 1 - 启动网关服务
   cd visPro-gateway
   mvn spring-boot:run
   
   # 终端 2 - 启动认证服务
   cd visPro-auth
   mvn spring-boot:run
   
   # 终端 3 - 启动系统服务
   cd visPro-system
   mvn spring-boot:run
   ```

### 方法 2: 使用 Docker Compose 运行（生产环境模式）

1. **构建并启动所有服务**
   ```bash
   docker-compose up --build
   ```

2. **或在后台运行**
   ```bash
   docker-compose up --build -d
   ```

## 服务配置

项目使用不同的配置文件：

- **dev**: 开发环境 (bootstrap-dev.yml)
- **prod**: 生产环境 (bootstrap-prod.yml)

每个服务都可以使用其各自的 bootstrap.yml 文件进行配置。

## 服务和端口

| 服务 | 端口 | 描述 |
|---------|------|-------------|
| visPro-gateway | 8080 | API 网关 - 所有请求的入口点 |
| visPro-auth | 9001 | 认证服务 |
| visPro-system | 9005 | 主系统服务，包含AI功能 |

## 功能

visPro 系统包含多种AI驱动的功能：

- **图像增强**: AI驱动的图像质量增强
- **图像识别**: 高级图像识别功能
- **OCR（光学字符识别）**: 从图像中提取文本
- **数学公式识别**: 数学表达式的OCR
- **医疗报告分析**: 医疗报告和体检报告的OCR
- **聊天界面**: 具有记忆功能的AI驱动对话系统
- **图像理解**: AI驱动的图像分析和描述

## 配置

系统支持与百度AI服务集成，用于以下功能：
- 图像增强
- 图像识别
- OCR服务
- 聊天功能

这些服务的配置可以在系统模块中的 `BDConfig.java` 文件中找到。

## 数据库架构

系统使用 MySQL 存储：
- 用户信息
- 对话历史
- 其他持久化数据

## API 端点

所有外部请求应通过网关 `http://localhost:8080` 进行。

## 开发

要为项目做贡献：

1. Fork 仓库
2. 创建功能分支
3. 进行修改
4. 如适用，添加测试
5. 提交拉取请求

## 故障排除

- 确保所有服务按正确顺序启动（网关、认证、系统）
- 检查数据库和 Redis 连接是否正确配置
- 验证所有必需的环境变量都已设置
- 检查日志中的任何错误消息

## 许可证

本项目根据 MIT 许可证授权 - 请参阅 LICENSE 文件了解详情。