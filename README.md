# Student API

## 项目介绍

这是一个基于spring boot开发的学生管理 REST API，
实现了学生信息的增加、修改、删除和查询，
并使用并使用 Redis 缓存提高按 ID 查询的性能，
通过空值标记减少对不存在数据的重复查询。

## 技术栈

- Java 17
- Spring Boot
- MyBatis
- MySQL
- Redis
- Maven

## 核心功能

- 学生信息的增删改查
- 按姓名和成绩范围动态查询
- 分页查询
- 查询及格学生
- 批量新增学生及事务回滚
- Redis 查询缓存与缓存失效
- 参数校验与全局异常处理
- 统一响应对象与异常响应格式

## API 接口

| 请求方法 | 接口地址 | 功能 |
| --- | --- | --- |
| GET | `/health` | 服务健康检查 |
| GET | `/students` | 查询全部学生 |
| GET | `/students/{id}` | 根据 ID 查询学生 |
| POST | `/students` | 新增学生 |
| PUT | `/students/{id}` | 修改指定学生 |
| DELETE | `/students/{id}` | 删除指定学生 |
| GET | `/students/passed` | 查询及格学生 |
| GET | `/students/search` | 多条件分页查询 |
| POST | `/students/batch` | 批量新增学生 |

动态查询参数说明：
name、minScore、maxScore，可选查询条件
page，页码数
pageSize，每页数量
示例:
http://localhost:8080/students/search?name=张&minScore=60&maxScore=100&page=1&pageSize=10

## 运行环境

- JDK 17
- MySQL 8
- Redis
- Maven（也可以使用项目自带的 Maven Wrapper）

## 环境变量

启动项目前需要配置以下环境变量：

| 环境变量 | 说明 | 默认值 |
| --- | --- | --- |
| `DB_PASSWORD` | MySQL 密码 | 无，必须提供 |
| `DB_URL` | MySQL 连接地址 | `jdbc:mysql://localhost:3306/student_db?serverTimezone=Asia/Shanghai&characterEncoding=UTF-8` |
| `DB_USERNAME` | MySQL 用户名 | `root` |
| `REDIS_HOST` | Redis 主机地址 | `localhost` |
| `REDIS_PORT` | Redis 端口 | `6379` |
| `SERVER_PORT` | 应用端口 | `8080` |

## 数据库初始化

执行以下 SQL 创建数据库和学生表：

```sql
CREATE DATABASE IF NOT EXISTS student_db
    DEFAULT CHARACTER SET utf8mb4;

USE student_db;

CREATE TABLE IF NOT EXISTS student (
    id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    score DECIMAL(5, 2) NOT NULL,
    PRIMARY KEY (id)
);
```

当前 `id` 不是自增字段，因此新增学生时需要在请求体中提供一个未被使用的 ID。


## 启动方式

### 1. 克隆项目

```powershell
git clone https://github.com/heryorker/student-api.git
cd student-api
```

### 2. 准备基础服务

1. 启动 MySQL，并创建名为 `student_db` 的数据库。
2. 启动 Redis，默认连接地址为 `localhost:6379`。

### 3. 配置数据库密码

PowerShell 临时环境变量：

```powershell
$env:DB_PASSWORD="<your-password>"
```

也可以在 IDEA 的 Spring Boot 运行配置中添加：

```text
DB_PASSWORD=<your-password>
```

### 4. 启动项目

Windows：

```powershell
.\mvnw.cmd spring-boot:run
```

启动成功后访问：

```text
GET http://localhost:8080/health
```

## 响应示例

根据 ID 查询学生成功：

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "id": 1,
    "name": "韩立",
    "score": 96.0
  }
}
```

学生不存在：

```json
{
  "code": 404,
  "message": "学生不存在",
  "data": null
}
```

参数校验失败：

```json
{
  "code": 400,
  "message": "参数校验失败",
  "data": {
    "name": "学生姓名不能为空",
    "score": "成绩不能大于100"
  }
}
```
