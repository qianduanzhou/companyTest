# 启动与排错

## 1. 启动顺序

这个项目依赖外部中间件，启动顺序很重要：

1. 启动 MySQL
2. 初始化数据库
3. 启动 ZooKeeper
4. 启动 `company-provider`
5. 启动 `company-consumer`

如果顺序不对，常见现象有：

- consumer 启动后接口调不通
- provider 注册不到 ZooKeeper
- provider 启动时报数据库连接失败

## 2. 数据库初始化

先执行：

- [`company-provider/src/main/resources/db/schema.sql`](/D:/project/customer/java/companyTest/company-provider/src/main/resources/db/schema.sql)
- [`company-provider/src/main/resources/db/data.sql`](/D:/project/customer/java/companyTest/company-provider/src/main/resources/db/data.sql)

数据库默认配置：

- 地址：`localhost:3306`
- 库名：`company_test`
- 用户名：`root`
- 密码：`root`

如果你的本机密码不是 `root`，请修改：

- [`company-provider/src/main/resources/application.yml`](/D:/project/customer/java/companyTest/company-provider/src/main/resources/application.yml)

## 3. ZooKeeper 配置

默认注册中心地址：

- `zookeeper://localhost:2181`

对应配置文件：

- [`company-provider/src/main/resources/application.yml`](/D:/project/customer/java/companyTest/company-provider/src/main/resources/application.yml#L30)
- [`company-consumer/src/main/resources/application.yml`](/D:/project/customer/java/companyTest/company-consumer/src/main/resources/application.yml#L8)

如果你本地不是这个地址，需要两个模块一起改。

## 4. IDEA 启动方式

### 启动 provider

启动类：

- [`ProviderApplication.java`](/D:/project/customer/java/companyTest/company-provider/src/main/java/com/company/provider/ProviderApplication.java)

成功标志：

- 应用正常启动
- 日志没有数据库连接报错
- 日志没有 ZooKeeper 连接报错
- Dubbo 成功暴露 `UserService`

### 启动 consumer

启动类：

- [`ConsumerApplication.java`](/D:/project/customer/java/companyTest/company-consumer/src/main/java/com/company/consumer/ConsumerApplication.java)

成功标志：

- 应用正常启动
- 访问 `http://localhost:8082/user/list` 能得到 JSON 响应

## 5. 如何验证是否真的跑通

推荐按这个顺序验证：

1. 先访问 `GET /user/list`
2. 再调用 `POST /user` 新增一条数据
3. 再调用 `GET /user/list` 确认新增成功
4. 再测试更新和删除

现成请求示例：

- [`http/user-api.http`](/D:/project/customer/java/companyTest/http/user-api.http)

## 6. 常见错误与排查思路

### 1) `mvn` 或 `java` 命令不可用

排查点：

- JDK 是否使用 1.8
- 环境变量是否生效
- IDEA 的 Project SDK 和 Maven JDK 是否都是 1.8

### 2) provider 启动失败，提示数据库连接异常

排查点：

- MySQL 是否启动
- 库 `company_test` 是否存在
- 用户名密码是否正确
- 3306 端口是否可访问

### 3) provider 启动失败，提示 ZooKeeper 连接异常

排查点：

- ZooKeeper 是否启动
- 2181 端口是否开放
- 配置地址是否写对

### 4) consumer 启动了，但调用接口超时

排查点：

- provider 是否真的启动成功
- provider 是否注册到了 ZooKeeper
- consumer 和 provider 是否使用同一个注册中心地址

### 5) 数据插入成功但字段映射不对

排查点：

- 表字段是否是下划线风格
- 实体字段是否是驼峰风格
- `map-underscore-to-camel-case` 是否开启

## 7. 一条建议

第一次学习时，不要一上来就“边改代码边排错”。

更高效的方式是：

1. 先完全跑通原项目
2. 再做小改动
3. 每改一处都重新验证

这样你会更容易建立“配置 -> 注册 -> 调用 -> 落库”的整体认知。
