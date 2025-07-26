# Hello API 使用範例

Hello API 提供同步和非同步的問候訊息處理功能，支援輸入驗證、快取和監控。

## 基本資訊

- **Base URL**: `http://localhost:8080/api/v1alpha`
- **Content-Type**: `application/json`
- **API 版本**: v1alpha

## 認證

所有 API 請求都需要包含以下 Headers：

```http
X-Tenant-Id: your-tenant-id
X-AppName: your-app-name
X-RoleName: your-role-name
```

## API 端點

### 1. 同步問候 (GET)

立即返回一個預設的問候訊息。

#### 請求

```bash
curl -X GET "http://localhost:8080/api/v1alpha/hello" \
  -H "Accept: application/json" \
  -H "X-Tenant-Id: t-123" \
  -H "X-AppName: asimov" \
  -H "X-RoleName: admin"
```

#### 回應

```json
{
  "message": {
    "message": "Hello, World!"
  },
  "launchTime": "2024-01-20T10:30:00.000Z"
}
```

### 2. 非同步問候 (POST)

接收自訂問候訊息並進行非同步處理。

#### 請求

```bash
curl -X POST "http://localhost:8080/api/v1alpha/hello" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -H "X-Tenant-Id: t-123" \
  -H "X-AppName: asimov" \
  -H "X-RoleName: admin" \
  -H "X-Request-Mode: async" \
  -d '{
    "message": "Hello, Asimov!"
  }'
```

#### 回應

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "creationTime": 1705747800000,
  "state": "PENDING",
  "data": {
    "message": "Hello, Asimov!"
  },
  "finishedAt": null
}
```

## 使用範例

### JavaScript (Fetch API)

```javascript
// 同步問候
async function syncHello() {
  const response = await fetch('http://localhost:8080/api/v1alpha/hello', {
    method: 'GET',
    headers: {
      'Accept': 'application/json',
      'X-Tenant-Id': 't-123',
      'X-AppName': 'asimov',
      'X-RoleName': 'admin'
    }
  });
  
  const data = await response.json();
  console.log('Sync Hello:', data);
}

// 非同步問候
async function asyncHello(message) {
  const response = await fetch('http://localhost:8080/api/v1alpha/hello', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'X-Tenant-Id': 't-123',
      'X-AppName': 'asimov',
      'X-RoleName': 'admin',
      'X-Request-Mode': 'async'
    },
    body: JSON.stringify({ message })
  });
  
  const data = await response.json();
  console.log('Async Hello:', data);
  return data.id; // 返回任務 ID
}

// 使用範例
syncHello();
asyncHello('Hello, World from JavaScript!');
```

### Python (requests)

```python
import requests
import json

BASE_URL = "http://localhost:8080/api/v1alpha"
HEADERS = {
    "X-Tenant-Id": "t-123",
    "X-AppName": "asimov",
    "X-RoleName": "admin"
}

def sync_hello():
    """同步問候"""
    response = requests.get(
        f"{BASE_URL}/hello",
        headers={**HEADERS, "Accept": "application/json"}
    )
    return response.json()

def async_hello(message):
    """非同步問候"""
    response = requests.post(
        f"{BASE_URL}/hello",
        headers={
            **HEADERS,
            "Content-Type": "application/json",
            "Accept": "application/json",
            "X-Request-Mode": "async"
        },
        json={"message": message}
    )
    return response.json()

# 使用範例
if __name__ == "__main__":
    # 同步問候
    sync_result = sync_hello()
    print("Sync Hello:", sync_result)
    
    # 非同步問候
    async_result = async_hello("Hello, World from Python!")
    print("Async Hello:", async_result)
    print("Task ID:", async_result["id"])
```

### Java (Spring RestTemplate)

```java
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

public class HelloApiClient {
    
    private static final String BASE_URL = "http://localhost:8080/api/v1alpha";
    private final RestTemplate restTemplate = new RestTemplate();
    
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Tenant-Id", "t-123");
        headers.set("X-AppName", "asimov");
        headers.set("X-RoleName", "admin");
        return headers;
    }
    
    public Map<String, Object> syncHello() {
        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
            BASE_URL + "/hello",
            HttpMethod.GET,
            entity,
            Map.class
        );
        
        return response.getBody();
    }
    
    public Map<String, Object> asyncHello(String message) {
        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Request-Mode", "async");
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("message", message);
        
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
            BASE_URL + "/hello",
            HttpMethod.POST,
            entity,
            Map.class
        );
        
        return response.getBody();
    }
    
    // 使用範例
    public static void main(String[] args) {
        HelloApiClient client = new HelloApiClient();
        
        // 同步問候
        Map<String, Object> syncResult = client.syncHello();
        System.out.println("Sync Hello: " + syncResult);
        
        // 非同步問候
        Map<String, Object> asyncResult = client.asyncHello("Hello, World from Java!");
        System.out.println("Async Hello: " + asyncResult);
        System.out.println("Task ID: " + asyncResult.get("id"));
    }
}
```

## 錯誤處理

### 驗證錯誤 (400 Bad Request)

```json
{
  "error": "Validation Failed",
  "message": "Message length must be at least 1 characters",
  "details": [
    "Message cannot be empty or contain only whitespace"
  ]
}
```

### 安全威脅檢測 (422 Unprocessable Entity)

```json
{
  "error": "Security Threat Detected",
  "message": "Message cannot contain HTML tags",
  "threatType": "xss_attempt"
}
```

### 內部伺服器錯誤 (500 Internal Server Error)

```json
{
  "error": "Internal Server Error",
  "message": "Failed to process hello request"
}
```

## 輸入驗證規則

### 訊息內容限制

- **長度**: 1-500 字符
- **禁用關鍵字**: spam, test123, admin
- **安全檢查**: 不能包含 HTML 標籤、SQL 關鍵字
- **格式檢查**: 不能全部是相同字符、不能全部大寫（超過10字符時）

### 有效訊息範例

```json
// ✅ 有效
{"message": "Hello, World!"}
{"message": "你好，世界！"}
{"message": "Hello! How are you? 😊"}
{"message": "Hello 2024!"}

// ❌ 無效
{"message": ""}                           // 空訊息
{"message": "   "}                        // 只有空白
{"message": "aaaaaaaaaa"}                 // 全部相同字符
{"message": "THIS IS ALL UPPERCASE TEXT"} // 全部大寫（超過10字符）
{"message": "Hello <script>alert('xss')</script>"} // 包含HTML標籤
{"message": "Hello spam content"}         // 包含禁用關鍵字
```

## 監控指標

API 提供以下監控指標（通過 `/actuator/prometheus` 端點）：

- `hello_requests_sync_total` - 同步請求總數
- `hello_requests_async_total` - 非同步請求總數
- `hello_processing_sync_seconds` - 同步處理時間
- `hello_processing_async_seconds` - 非同步處理時間
- `hello_validation_failures_total` - 驗證失敗總數
- `hello_cache_hits_total` - 快取命中總數
- `hello_cache_misses_total` - 快取未命中總數
- `hello_security_threats_total` - 安全威脅檢測總數

## 最佳實踐

1. **錯誤處理**: 始終檢查 HTTP 狀態碼和回應內容
2. **重試機制**: 對於 5xx 錯誤實作指數退避重試
3. **超時設定**: 設定適當的請求超時時間
4. **日誌記錄**: 記錄請求和回應以便除錯
5. **監控**: 監控 API 回應時間和錯誤率
6. **安全**: 不要在訊息中包含敏感資訊

## 支援

如有問題或建議，請聯繫開發團隊或查看 Swagger UI 文件：
`http://localhost:8080/swagger-ui.html`