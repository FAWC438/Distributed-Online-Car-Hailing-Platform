## /customer

## /customer/register

#### 接口URL

> /register?customerName=lwm&password=lwm&email=lwm

#### 请求方式

> GET

#### Content-Type

> form-data

#### 请求Query参数

| 参数名       | 示例值 | 参数类型 | 是否必填 | 参数描述 |
| ------------ | ------ | -------- | -------- | -------- |
| customerName | lwm    | Text     | 是       | 用户名称 |
| password     | lwm    | Text     | 是       | 用户密码 |
| email        | lwm    | Text     | 是       | 用户邮箱 |

## /customer/login

#### 接口URL

> /login?customerName=lwm&password=lwm

#### 请求方式

> POST

#### Content-Type

> form-data

#### 请求Query参数

| 参数名       | 示例值 | 参数类型 | 是否必填 | 参数描述 |
| ------------ | ------ | -------- | -------- | -------- |
| customerName | lwm    | Text     | 是       | 用户名称 |
| password     | lwm    | Text     | 是       | 用户密码 |

## /customer/logout

#### 接口URL

> /logout?customerName=lwm

#### 请求方式

> POST

#### Content-Type

> form-data

#### 请求Query参数

| 参数名       | 示例值 | 参数类型 | 是否必填 | 参数描述 |
| ------------ | ------ | -------- | -------- | -------- |
| customerName | lwm    | Text     | 是       | 用户名称 |

## /customer/updateCustomer

#### 接口URL

> /updateCustomer?customerName=lwm&curX=25&curY=25

#### 请求方式

> POST

#### Content-Type

> form-data

#### 请求Query参数

| 参数名       | 示例值 | 参数类型 | 是否必填 | 参数描述 |
| ------------ | ------ | -------- | -------- | -------- |
| customerName | lwm    | Text     | 是       | 用户名称 |
| curX         | 25     | Text     | 是       | 当前位置 |
| curY         | 25     | Text     | 是       | 当前位置 |

## /customer/hailing

#### 接口状态

> 开发中

#### 接口URL

> /Hailing?customerName=lwm&desX=0&desY=0&serviceLevel=0

#### 请求方式

> GET

#### Content-Type

> form-data

#### 请求Query参数

| 参数名       | 示例值 | 参数类型 | 是否必填 | 参数描述     |
| ------------ | ------ | -------- | -------- | ------------ |
| customerName | lwm    | Text     | 是       | 用户名称     |
| desX         | 0      | Text     | 是       | 目标位置     |
| desY         | 0      | Text     | 是       | 目标位置     |
| serviceLevel | 0      | Text     | 是       | 服务最低等级 |

## /customer/finishOrder

#### 接口状态

> 开发中

#### 接口URL

> /finishOrder?customerName=lwm&content=nice&commentLevel=5

#### 请求方式

> GET

#### Content-Type

> form-data

#### 请求Query参数

| 参数名       | 示例值 | 参数类型 | 是否必填 | 参数描述 |
| ------------ | ------ | -------- | -------- | -------- |
| customerName | lwm    | Text     | 是       | 用户名称 |
| content      | nice   | Text     | 是       | 评价内容 |
| commentLevel | 5      | Text     | 是       | 评价等级 |

## /customer/searchOrder

#### 接口状态

> 开发中

#### 接口URL

> /searchOrder?customerName=lwm

#### 请求方式

> GET

#### Content-Type

> form-data

#### 请求Query参数

| 参数名       | 示例值 | 参数类型 | 是否必填 | 参数描述 |
| ------------ | ------ | -------- | -------- | -------- |
| customerName | lwm    | Text     | 是       | 用户名称 |

## /customer/searchDriver

#### 接口状态

> 开发中

#### 接口URL

> /searchDriver?driverName=lwm2

#### 请求方式

> GET

#### Content-Type

> form-data

#### 请求Query参数

| 参数名     | 示例值 | 参数类型 | 是否必填 | 参数描述 |
| ---------- | ------ | -------- | -------- | -------- |
| driverName | lwm2   | Text     | 是       | 司机名称 |