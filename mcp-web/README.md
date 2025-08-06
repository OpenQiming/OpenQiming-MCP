
# MCP广场项目

MCP广场是一个基于MCP协议的服务接入与管理平台，包含以下子模块：

- `mcp-server`：后端服务模块
- `mcp-web`：前端展示模块

主要功能包括：

- MCP广场首页展示
- MCP Server 详情页
- MCP接入方式页面

---


## ⚙️ 部署环境要求

### 后端环境（`mcp-server`）

- JDK 1.8
- Maven

### 前端环境（`mcp-web`）

- Node.js ≥ 14
- npm

---

## 🛢️ 数据库配置

本项目使用 **PostgreSQL** 数据库，部署前请完成以下步骤：

1. 安装 PostgreSQL
2. 创建数据库：
    - 数据库名：`aes_db`
3. 创建 schema：
    - Schema 名：`aes_sc`
4. 导入建表脚本：
   ```sql
   -- 使用 tables_mcp_server.sql 文件执行建表操作
   ```

---

## 🔧 后端配置说明

请根据实际环境修改后端配置文件：

文件路径：
```
mcp-server/src/main/resources/application-newhg.yml
```

需配置项：
- 数据库连接地址
- 用户名/密码
- 端口号等基础信息

---

## 🚀 启动项目

### 启动后端（MCP Server）

```bash
# 在 mcp-server 根目录执行
mvn clean install
java -jar target/mcp-server.jar
```

或在 IDE 中运行主类

---

### 启动前端（MCP Web）

```bash
# 在 mcp-web 目录下执行：
npm install       # 安装依赖
npm run dev       # 启动开发服务器（本地调试）

# 或打包部署使用：
npm run build
```

---

## 📌 注意事项

- 后端和前端配置需保持 API 路径一致
- 如需资源池部署，请将打包后的文件部署至对应平台
- 数据库务必确保 schema 和建表语句完整执行


