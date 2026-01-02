# NAS CCTV

基于 Spring Boot + Vue 的网络摄像头管理系统示例，使用 MySQL 8.0 与 MyBatis 进行数据持久化。

## 功能概览

- 多摄像头资产管理（ONVIF / RTSP / GB/T 28181 等协议）
- 摄像头多码流接入与录像存储登记
- 电视墙展示与视频轮播（按用户权限过滤通道）
- JWT 身份鉴权与基础权限控制
- 用户管理与摄像头权限隔离
- 现代化 UI 仪表盘示例

## 后端 (Spring Boot)

```bash
cd backend
mvn spring-boot:run
```

> 默认数据库配置在 `backend/src/main/resources/application.yml` 中。

## 前端 (Vue + Vite)

```bash
cd frontend
npm install
npm run dev
```

## 数据库初始化

后端启动时会执行 `backend/src/main/resources/schema.sql`，需提前创建 `nascctv` 数据库并配置账号密码。

### 默认账户

系统启动会自动创建默认管理员账号：

- 用户名：`admin`
- 密码：`admin`

## 录像存储说明

示例后端提供录像登记接口 `/api/recordings`，用于记录录像文件的存储路径、格式、大小与留存周期。
