## /driver

## /driver/register

#### 接口URL

> /register?driverName=lwm&password=lwm&email=lwm

#### 请求方式

> POST

#### Content-Type

> form-data

#### 请求Query参数

| 参数名     | 示例值 | 参数类型 | 是否必填 | 参数描述 |
| ---------- | ------ | -------- | -------- | -------- |
| driverName | lwm    | Text     | 是       | 司机名称 |
| password   | lwm    | Text     | 是       | 密码     |
| email      | lwm    | Text     | 是       | 邮箱     |

## /driver/login

#### 接口URL

> /login?driverName=lwm&password=lwm

#### 请求方式

> POST

#### Content-Type

> form-data

#### 请求Query参数

| 参数名     | 示例值 | 参数类型 | 是否必填 | 参数描述 |
| ---------- | ------ | -------- | -------- | -------- |
| driverName | lwm    | Text     | 是       | 司机名称 |
| password   | lwm    | Text     | 是       | 登录密码 |

## /driver/edit

#### 接口URL

> /edit?driverName=lwm&serviceLevel=10

#### 请求方式

> POST

#### Content-Type

> form-data

#### 请求Query参数

| 参数名       | 示例值 | 参数类型 | 是否必填 | 参数描述 |
| ------------ | ------ | -------- | -------- | -------- |
| driverName   | lwm    | Text     | 是       | 司机名称 |
| serviceLevel | 10     | Text     | 是       | 服务等级 |

## /driver/updateDriver

#### 接口URL

> /updateDriver?driverName=lwm

#### 请求方式

> POST

#### Content-Type

> form-data

#### 请求Query参数

| 参数名     | 示例值 | 参数类型 | 是否必填 | 参数描述 |
| ---------- | ------ | -------- | -------- | -------- |
| driverName | lwm    | Text     | 是       | -        |

## /driver/handleRequestOrder

#### 接口URL

> /handleRequestOrder?driverName=lwm&orderNum=0

#### 请求方式

> POST

#### Content-Type

> form-data

#### 请求Query参数

| 参数名     | 示例值 | 参数类型 | 是否必填 | 参数描述 |
| ---------- | ------ | -------- | -------- | -------- |
| driverName | lwm    | Text     | 是       | 司机名称 |
| orderNum   | 0      | Text     | 是       | 订单编号 |

## /driver/takeCustomer

#### 接口URL

> /takeCustomer?driverName=lwm

#### 请求方式

> POST

#### Content-Type

> form-data

#### 请求Query参数

| 参数名     | 示例值 | 参数类型 | 是否必填 | 参数描述 |
| ---------- | ------ | -------- | -------- | -------- |
| driverName | lwm    | Text     | 是       | 司机名称 |

## /driver/finishOrder

#### 接口URL

> /finishOrder?driverName=lwm1

#### 请求方式

> POST

#### Content-Type

> form-data

#### 请求Query参数

| 参数名     | 示例值 | 参数类型 | 是否必填 | 参数描述 |
| ---------- | ------ | -------- | -------- | -------- |
| driverName | lwm1   | Text     | 是       | 司机名称 |

## /driver/searchOrder

#### 接口URL

> /searchOrder?driverName=lwm

#### 请求方式

> GET

#### Content-Type

> form-data

#### 请求Query参数

| 参数名     | 示例值 | 参数类型 | 是否必填 | 参数描述 |
| ---------- | ------ | -------- | -------- | -------- |
| driverName | lwm    | Text     | 是       | 司机名称 |