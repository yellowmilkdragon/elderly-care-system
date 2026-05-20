# 东软颐养中心管理系统

## 1. 项目说明

本项目依据《东软颐养中心需求规格说明书》与《东软颐养中心管理系统数据库设计》完成，是一个面向颐养中心业务场景的前后端分离管理系统。

系统围绕以下核心业务展开：

- 客户入住登记与档案管理
- 床位状态展示与床位调换
- 护理项目与护理等级管理
- 健康管家服务对象分配
- 外出审批、返院登记、退住审批
- 用户与角色管理

## 2. 技术栈

### 2.1 前端

- Vue 3
- Vite
- Vue Router
- Pinia
- Axios
- Element Plus

### 2.2 后端

- Spring Boot 3
- Spring Security
- MyBatis-Plus
- Maven

## 3. 项目结构

```text
java_big_work
├─ backend                后端 Spring Boot 工程
├─ frontend               前端 Vue 工程
├─ README.md              项目说明文档
├─ 东软颐养中心管理系统-系统设计方案.md
├─ 01_requirements.txt    需求说明文本整理稿
└─ 02_database.txt        数据库设计文本整理稿
```

## 4. 当前已完成内容

### 4.1 前端

- 登录页
- 工作台首页
- 客户管理
- 床位管理
- 护理项目管理
- 护理等级管理
- 审批管理
- 健康管家管理
- 用户管理

### 4.2 后端

- 登录认证接口
- 工作台统计接口
- 客户管理接口
- 床位管理接口
- 护理项目与护理等级接口
- 审批接口
- 健康管家分配接口
- 用户查询接口

### 4.3 当前实现说明

- 当前版本以教学演示为主
- 后端业务数据暂采用内存模拟存储
- 已支持主要页面交互、审批操作、床位调换和服务对象分配

## 5. 开发环境

根据老师作业要求，推荐环境如下：

- 开发工具：IntelliJ IDEA
- JDK：17 及以上
- Maven：3.9.x
- Node.js：18 及以上
- npm：9 及以上
- 数据库：MySQL 8.x（当前演示版尚未接入真实持久化）

## 6. IDEA 打开方式

如果要在 IDEA 中查看和管理代码，建议这样操作：

### 6.1 查看整个项目

直接打开根目录：

```text
C:\Users\hyk\Desktop\java_big_work
```

### 6.2 主要查看后端代码

直接打开：

```text
C:\Users\hyk\Desktop\java_big_work\backend
```

IDEA 会自动识别为 Maven 工程。

### 6.3 查看前端代码

前端工程目录：

```text
C:\Users\hyk\Desktop\java_big_work\frontend
```

## 7. 运行方式

### 7.1 启动后端

```bash
cd backend
mvn spring-boot:run
```

默认接口地址：

```text
http://localhost:8081/api
```

### 7.2 启动前端

```bash
cd frontend
npm install
npm run dev
```

默认访问地址：

```text
http://localhost:5173
```

## 8. 功能模块对应作业要求

结合需求文档，本项目已覆盖的主要作业模块如下：

- 客户管理：入住登记、信息维护
- 床位管理：床位展示、床位调换
- 护理模块：护理项目、护理等级
- 审批模块：外出审批、返院登记、退住审批
- 健康管家：服务对象分配
- 用户管理：基础账号信息展示

## 9. 当前不足

从正式课程作业标准来看，目前仍有以下提升空间：

- 中文文案和部分历史文件仍需继续清理
- 后端仍为演示型内存数据，未完全接入 MySQL
- 权限控制、菜单权限、真实角色隔离仍可继续完善
- 数据库建表 SQL 与初始化脚本尚未整理成正式交付版

## 10. 提交建议

如果作为课程作业提交，建议至少包含以下内容：

- 源代码工程
- 数据库设计文档
- 系统设计说明文档
- 运行说明
- 功能截图

## 11. 附件说明

- [系统设计方案](C:\Users\hyk\Desktop\java_big_work\东软颐养中心管理系统-系统设计方案.md)
- [需求整理稿](C:\Users\hyk\Desktop\java_big_work\01_requirements.txt)
- [数据库整理稿](C:\Users\hyk\Desktop\java_big_work\02_database.txt)
