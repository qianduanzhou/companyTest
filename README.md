# company-test

一个用于学习 `Spring Boot + Dubbo + ZooKeeper + MyBatis-Plus` 的多模块示例项目。

这个项目的目标不是堆很多业务，而是用一条尽量短、但已经比较完整的链路，把下面几个核心知识点串起来：

- Spring Boot 如何组织一个后端项目
- Maven 多模块项目如何拆分职责
- Dubbo 如何定义服务、暴露服务、消费服务
- ZooKeeper 在 Dubbo 中如何充当注册中心
- MyBatis-Plus 如何完成基础 CRUD 和分页
- Consumer 如何把 Dubbo 服务包装成 HTTP 接口
- 参数校验、统一返回体、统一异常处理如何落到项目里

## 1. 项目结构

```text
company-test
├─ company-api       公共契约层：实体、DTO、统一返回体、Dubbo 接口
├─ company-provider  服务提供者：实现 Dubbo 接口，访问 MySQL
├─ company-consumer  服务消费者：暴露 REST 接口，调用 Dubbo 服务
└─ http              HTTP 请求示例
```

模块依赖关系：

```text
company-consumer -> company-api
company-provider -> company-api
```

运行时调用链路：

```text
HTTP 请求
   -> company-consumer
   -> Dubbo RPC
   -> company-provider
   -> MyBatis-Plus
   -> MySQL
```

## 2. 技术栈

- JDK 8
- Spring Boot 2.7.18
- Dubbo 3.2.9
- ZooKeeper
- MyBatis-Plus 3.5.5
- MySQL
- Lombok

## 3. 当前实现了什么

当前项目围绕“用户管理”实现了一套可运行闭环：

- `GET /user/{id}`：按 ID 查询用户
- `GET /user/list`：查询全部用户
- `GET /user/page`：分页查询用户，支持 `keyword` 按用户名/手机号模糊搜索
- `POST /user`：新增用户
- `PUT /user`：更新用户
- `DELETE /user/{id}`：删除用户（逻辑删除）

除此之外，项目里还已经包含：

- 参数校验（DTO 校验、路径参数校验）
- 统一返回体 `Result<T>`
- 通用分页返回 `PageResult<T>`
- 全局异常处理
- 用户名唯一校验
- MyBatis-Plus 分页插件

## 4. 核心代码位置

### company-api

- 实体：`company-api/src/main/java/com/company/api/entity/User.java`
- DTO：`company-api/src/main/java/com/company/api/dto/UserDTO.java`
- 分页 DTO：`company-api/src/main/java/com/company/api/dto/UserPageQueryDTO.java`
- 服务接口：`company-api/src/main/java/com/company/api/service/UserService.java`
- 统一返回体：`company-api/src/main/java/com/company/api/result/Result.java`
- 分页返回体：`company-api/src/main/java/com/company/api/result/PageResult.java`

### company-provider

- 启动类：`company-provider/src/main/java/com/company/provider/ProviderApplication.java`
- 服务实现：`company-provider/src/main/java/com/company/provider/service/UserServiceImpl.java`
- Mapper：`company-provider/src/main/java/com/company/provider/mapper/UserMapper.java`
- MyBatis-Plus 配置：`company-provider/src/main/java/com/company/provider/config/MybatisPlusConfig.java`
- 配置文件：`company-provider/src/main/resources/application.yml`
- 数据库脚本：`company-provider/src/main/resources/db/schema.sql`
- 初始化数据：`company-provider/src/main/resources/db/data.sql`

### company-consumer

- 启动类：`company-consumer/src/main/java/com/company/consumer/ConsumerApplication.java`
- 控制器：`company-consumer/src/main/java/com/company/consumer/controller/UserController.java`
- 全局异常处理：`company-consumer/src/main/java/com/company/consumer/handler/GlobalExceptionHandler.java`
- 配置文件：`company-consumer/src/main/resources/application.yml`

## 5. 启动前准备

你需要先准备这几个环境：

1. JDK 8
2. Maven 3.9+
3. MySQL 8.x
4. ZooKeeper 3.7+

当前代码中的默认配置如下：

- MySQL：`localhost:3306/company_test`
- MySQL 用户名：`root`
- MySQL 密码：`78000`
- ZooKeeper：`localhost:2181`
- Provider HTTP 端口：`8081`
- Provider Dubbo 端口：`20881`
- Consumer HTTP 端口：`8082`

如果你的本机配置不同，请优先修改：

- `company-provider/src/main/resources/application.yml`
- `company-consumer/src/main/resources/application.yml`

## 6. 最短启动步骤

1. 在 MySQL 中执行 `company-provider/src/main/resources/db/schema.sql`
2. 再执行 `company-provider/src/main/resources/db/data.sql`
3. 启动 ZooKeeper，确认 `2181` 端口可用
4. 启动 `ProviderApplication`
5. 启动 `ConsumerApplication`
6. 访问 `http://localhost:8082/user/list`

## 7. 接口示例

### 查询全部用户

```http
GET http://localhost:8082/user/list
Accept: application/json
```

### 分页查询用户

```http
GET http://localhost:8082/user/page?pageNum=1&pageSize=2&keyword=to
Accept: application/json
```

### 按 ID 查询用户

```http
GET http://localhost:8082/user/1
Accept: application/json
```

### 新增用户

```http
POST http://localhost:8082/user
Content-Type: application/json

{
  "username": "alice",
  "email": "alice@example.com",
  "phone": "13800000001"
}
```

### 更新用户

```http
PUT http://localhost:8082/user
Content-Type: application/json

{
  "id": 1,
  "username": "alice-updated",
  "email": "alice.updated@example.com",
  "phone": "13800000009"
}
```

### 删除用户

```http
DELETE http://localhost:8082/user/1
Accept: application/json
```

> 项目中还提供了 HTTP Client 示例文件：`http/user-api.http`。
> 如果你直接使用该文件，请先确认其中端口是否与当前配置一致。

## 8. 返回结果格式

统一返回格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

分页接口返回示例：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 3,
    "pageNum": 1,
    "pageSize": 2,
    "records": []
  }
}
```

## 9. 推荐学习顺序

建议按下面顺序看，不容易乱：

1. 先看根 `pom.xml`，理解多模块结构和依赖统一管理
2. 再看 `company-api`，理解“接口先行”的设计
3. 然后看 `company-provider`，理解数据库访问、分页和 Dubbo 服务暴露
4. 再看 `company-consumer`，理解 HTTP 接口如何转 Dubbo 调用，以及异常处理
5. 最后结合 ZooKeeper 和 MySQL，把整个调用链跑通

## 10. 配套文件

- `docs/01-project-overview.md`
- `docs/02-startup-and-debug.md`
- `docs/03-learning-roadmap.md`
- `http/user-api.http`

## 11. 一些学习建议

- 第一次先不要急着改代码，先把项目完整跑通
- 每看到一层，就先回答“这一层的职责是什么”
- 不要孤立看 Dubbo 或 MyBatis-Plus，要把“调用链”串起来理解
- 建议先调通 `GET /user/list`，再测试新增、分页、更新、删除
- 如果本机配置和仓库默认值不同，先改配置再排错

## 12. 下一步适合做什么

当你把当前项目跑通后，推荐继续自己动手做这几件事：

- 给分页查询增加更多筛选条件
- 给用户增加启用/禁用状态修改接口
- 把 `http/user-api.http` 同步为当前端口配置
- 给项目增加 Swagger / OpenAPI 文档
- 尝试把配置拆成 `dev/test` 多环境
- 用 Docker 补齐 MySQL 和 ZooKeeper 的启动方式
