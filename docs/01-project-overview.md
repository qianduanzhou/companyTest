# 项目总览

## 1. 为什么这样拆模块

这个项目采用 Maven 多模块，是为了让“契约”和“实现”分离，更贴近 Dubbo 项目的真实组织方式。

- `company-api`
  只放公共内容，不放业务实现
- `company-provider`
  负责实现服务接口，并连接数据库
- `company-consumer`
  负责对外暴露 HTTP 接口，并通过 Dubbo 调用 provider

这种拆法的好处是：

- 接口层可以被多个服务共同依赖
- Provider 和 Consumer 可以独立开发和部署
- 更容易理解“面向接口编程”和“服务调用”的边界

## 2. 核心调用链

以“查询用户列表”为例，调用链如下：

1. 浏览器或 Postman 调用 `GET /user/list`
2. 请求进入 `company-consumer` 的 `UserController`
3. `UserController` 通过 `@DubboReference` 调用远程 `UserService`
4. Dubbo 从 ZooKeeper 找到 provider 实例
5. 请求进入 `company-provider` 的 `UserServiceImpl`
6. `UserServiceImpl` 通过 `UserMapper` 查询 MySQL
7. 查询结果返回给 consumer
8. consumer 再把结果作为 HTTP 响应返回

## 3. 每层该关注什么

### company-api

这里重点看 4 类内容：

- 实体 `User`
- DTO `UserDTO`
- 统一返回体 `Result`
- Dubbo 接口 `UserService`

学习重点：

- 为什么服务接口放在公共模块
- DTO 和 Entity 为什么分开
- 统一返回体有什么好处

### company-provider

这里是业务实现层。

学习重点：

- `@EnableDubbo` 是怎么开启 Dubbo 的
- `@DubboService` 是怎么把服务暴露出去的
- `@MapperScan` 和 `UserMapper` 如何配合工作
- MyBatis-Plus 的 `BaseMapper` 为什么可以直接完成 CRUD

### company-consumer

这里是入口层。

学习重点：

- `@RestController` 如何提供 HTTP 接口
- `@DubboReference` 如何引用远程服务
- 为什么 consumer 不直接访问数据库

## 4. 这个项目故意保持简单的地方

为了便于入门，这个项目省略了很多企业项目常见内容，比如：

- 分层更细的 service / manager / assembler
- 统一异常处理
- 参数校验
- 分页查询
- 登录鉴权
- 缓存
- 链路追踪

这不是缺点，反而很适合学习第一版。

你先吃透这条最小链路，后面再逐步加复杂度，学习效率会更高。
