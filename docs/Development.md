

## Service

專注處理 Biz Logic,

1. 不處理組 DTO 的任務
2. 應該沒有其他的 import, 只有 POJO
3. 不處理 request / response 等 HTTP message


## Cache

- Redis --> 準備換成 Valkey 8.0
- 都用 String 存入
- 處理 JSON / Model 用 JsonUtils 處理


