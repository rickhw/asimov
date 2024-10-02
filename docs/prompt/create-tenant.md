
條件是  Java17, Springboot 3.2, 有個 REST API 如下：

```json
@contentType = application/json
@hostname = http://localhost:8080/api/tenants

### apply a new tenant
POST {{hostname}}
content-type: {{contentType}}

{
  "tenantKey": "this-is-a-tenant-uniqu",
  "description": "test",
  "rootAccount": "rick@abc.com"
}
```

這隻 API 主要是用來申請一個 tenant, 其中會提供 rootAccount 以及 描述.

這隻 API 正常的情境會回覆以下資訊:

1. 回覆 TenantId, 格式為 `t-1234567890`
2. Response Payload 如下：

```json
HTTP/1.1 202 Accepted

{
    "State": "Accepted",
    "Data": {
        "tenandId": "t-1234567890",
    },
    "TaskId": "<UUID>"
}
```

3. 使用者透過 Task API 取得執行結果, 如下
```http
GET /api/tasks/<UUID>
```

responsed:

```json
HTTP/1.1 200 OK

{
    "State": "Running",
    "Data": {
        "tenandId": "t-1234567890",
    },
    "TaskId": "<UUID>"
}
```

狀態完成, 取得的 Task response 如下:

```json
HTTP/1.1 200 OK

{
    "State": "Completed",
    "Data": {
        "kind": "core.Tenant",
        "metadata": {
            "tenandId": "t-1234567890",
            "tenantKey": "this-is-a-tenant-uniqu",
        },
        "domainSpec": {
            "description": "test",
            "rootAccount": "rick@abc.com"
        }
    },
    "TaskId": "<UUID>"
}
```

請協助提供完整的 Unit Test, 包含以下：

1. 這個 Unit Test 本身的 Test Data / expected Response 不要寫在程式裡面，而是放在 resources 目錄底下，用 test-data, expected-data 兩個目錄拆分
2. TenantController 的 Package 名稱為 package com.gtcafe.asimov.apiserver.tenant.TenantController


