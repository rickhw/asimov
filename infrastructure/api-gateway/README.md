在 Apache APISIX 中，可以使用 **proxy-rewrite** 插件來添加或修改 HTTP headers，並將它們轉發給上游服務 (upstream)。以下是一個獨立的 YAML 範例，展示如何配置一個 route 並添加自定義的 HTTP headers。

### YAML 配置範例

```yaml
routes:
  - uri: /example
    name: example-route
    upstream:
      type: roundrobin
      nodes:
        "httpbin.org:80": 1
    plugins:
      proxy-rewrite:
        headers:
          X-Custom-Header: "CustomValue"
          X-Forwarded-For: "$remote_addr"
```

### 說明
1. **`uri`**: 定義此路由匹配的請求路徑，這裡是 `/example`。
2. **`upstream`**: 定義上游服務，此例中上游服務是 `httpbin.org:80`。
3. **`plugins`**: 啟用插件。
   - **`proxy-rewrite`**: 
     - **`headers`**: 定義需要添加或覆蓋的 HTTP headers。
       - `X-Custom-Header`: 添加一個自定義的 header，值為固定的 `CustomValue`。
       - `X-Forwarded-For`: 設置此 header 的值為請求者的 IP 地址（使用內建變數 `$remote_addr`）。

### 如何測試
假設 APISIX 的入口地址是 `http://localhost:9080`，可以使用 cURL 測試此配置：
```bash
curl -i http://localhost:9080/example
```

在上游服務 (例如 `httpbin.org`) 中，您應該可以看到以下 headers：
```json
{
  "headers": {
    "X-Custom-Header": "CustomValue",
    "X-Forwarded-For": "127.0.0.1"
  }
}
```

### 注意事項
1. 請確保 `proxy-rewrite` 插件已啟用。
2. 如果 APISIX 是在分佈式模式下運行，請將配置上傳至 APISIX 的控制平面。
3. 如果需要更動 headers，請確保沒有其他插件覆蓋這些 headers。

如有更多需求，可隨時補充！