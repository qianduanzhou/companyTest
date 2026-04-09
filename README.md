# company-test

一个用于学习 `Spring Boot + Dubbo + ZooKeeper + MyBatis-Plus` 的多模块示例项目。

这个项目的目标不是堆很多业务，而是用一条尽量短的链路，把下面几个核心知识点串起来：

- Spring Boot 如何组织一个后端项目
- Maven 多模块项目如何拆分职责
- Dubbo 如何定义服务、暴露服务、消费服务
- ZooKeeper 在 Dubbo 中如何充当注册中心
- MyBatis-Plus 如何完成基础 CRUD
- Consumer 如何把 Dubbo 服务包装成 HTTP 接口

## 1. 项目结构

```text
company-test
├─ company-api       公共契约层：实体、DTO、统一返回体、Dubbo 接口
├─ company-provider  服务提供者：实现 Dubbo 接口，访问 MySQL
└─ company-consumer  服务消费者：暴露 REST 接口，调用 Dubbo 服务
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

当前项目围绕“用户管理”做了一套最小可运行闭环：

- `GET /user/{id}` 查询用户
- `GET /user/list` 查询全部用户
- `POST /user` 新增用户
- `PUT /user` 更新用户
- `DELETE /user/{id}` 删除用户

你可以把它理解成一个“最小教学样板”：

- `company-api` 负责定义契约
- `company-provider` 负责实现契约
- `company-consumer` 负责对外暴露接口

## 4. 启动前准备

你需要先准备这几个环境：

1. JDK 8
2. Maven 3.9+
3. MySQL 8.x
4. ZooKeeper 3.7+

当前默认配置如下：

- MySQL：`localhost:3306/company_test`
- MySQL 用户名：`root`
- MySQL 密码：`root`
- ZooKeeper：`localhost:2181`
- Provider HTTP 端口：`8081`
- Provider Dubbo 端口：`20881`
- Consumer HTTP 端口：`8080`

## 5. 最短启动步骤

1. 在 MySQL 中执行 [`company-provider/src/main/resources/db/schema.sql`](/D:/project/customer/java/companyTest/company-provider/src/main/resources/db/schema.sql)
2. 再执行 [`company-provider/src/main/resources/db/data.sql`](/D:/project/customer/java/companyTest/company-provider/src/main/resources/db/data.sql)
3. 启动 ZooKeeper，确认 `2181` 端口可用
4. 启动 `ProviderApplication`
5. 启动 `ConsumerApplication`
6. 访问 `http://localhost:8080/user/list`

## 6. 推荐学习顺序

建议按下面顺序学习，不容易乱：

1. 先看根 `pom.xml`，理解多模块结构和依赖统一管理
2. 再看 `company-api`，理解“接口先行”的设计
3. 然后看 `company-provider`，理解数据库访问和 Dubbo 服务暴露
4. 再看 `company-consumer`，理解 HTTP 接口如何转 Dubbo 调用
5. 最后结合 ZooKeeper 和 MySQL，把整个调用链跑通

## 7. 配套文档

- [`docs/01-project-overview.md`](/D:/project/customer/java/companyTest/docs/01-project-overview.md)
- [`docs/02-startup-and-debug.md`](/D:/project/customer/java/companyTest/docs/02-startup-and-debug.md)
- [`docs/03-learning-roadmap.md`](/D:/project/customer/java/companyTest/docs/03-learning-roadmap.md)
- [`http/user-api.http`](/D:/project/customer/java/companyTest/http/user-api.http)

## 8. 一些学习建议

- 第一次先不要急着改代码，先把项目完整跑通
- 每看到一层，就先回答“这一层的职责是什么”
- 不要孤立看 Dubbo 或 MyBatis-Plus，要把“调用链”串起来理解
- 能用 Postman 或 IDEA HTTP Client 调通接口后，再开始自己扩展功能

## 9. 下一步适合做什么

当你把当前项目跑通后，推荐继续自己动手做这几件事：

- 给 `User` 增加分页查询
- 给接口加参数校验
- 给返回结果增加统一异常处理
- 给 provider 增加 service 层和转换层
- 给项目增加日志链路和接口文档
- 尝试把配置拆成 `dev/test` 多环境

如果你愿意，我下一步还可以继续帮你把这个项目再升级成“更像真实项目”的学习版，比如补上：

- 全局异常处理
- 参数校验
- 分页查询
- 统一日志
- Docker 版 MySQL/ZooKeeper 启动说明
