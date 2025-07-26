# Hello Service 任務詳細說明

## Task #7: 統一佇列配置管理

### 7.1 統一配置類別
**目標**: 讓 Consumer 使用與 Producer 相同的配置

**工作內容**:
- 修改 `HelloConsumerAsyncConfig` 使用 `HelloQueueConfig`
- 移除硬編碼的佇列名稱常數
- 確保兩端使用相同的配置來源

**修改檔案**:
- `consumer/src/main/java/com/gtcafe/asimov/consumer/system/hello/config/HelloConsumerAsyncConfig.java`
- `consumer/src/main/java/com/gtcafe/asimov/consumer/system/hello/HelloConsumer.java`

### 7.2 動態配置支援
**目標**: Consumer 支援配置檔案動態調整

**工作內容**:
- 在 Consumer 中加入 `@ConfigurationProperties` 支援
- 實作配置變更監聽機制
- 加入配置驗證邏輯

### 7.3 配置驗證機制
**目標**: 防止 Producer 和 Consumer 配置不匹配

**工作內容**:
- 建立配置驗證工具
- 啟動時檢查配置一致性
- 配置不匹配時提供明確錯誤訊息

---

## Task #8: Consumer 端監控完善

### 8.1 建立 HelloConsumerMetricsService
**目標**: 為 Consumer 建立完整監控指標

**工作內容**:
```java
// 需要實作的指標
- hello_consumer_messages_processed_total
- hello_consumer_processing_duration_seconds
- hello_consumer_dlq_messages_total
- hello_consumer_task_state_transitions_total
- hello_consumer_errors_total
```

**新增檔案**:
- `consumer/src/main/java/com/gtcafe/asimov/consumer/system/hello/service/HelloConsumerMetricsService.java`

### 8.2 整合業務監控
**目標**: 與 Producer 端監控指標對應

**工作內容**:
- 實作端到端處理時間追蹤
- 加入分散式追蹤支援
- 建立健康檢查端點

### 8.3 監控儀表板
**目標**: 提供可視化監控介面

**工作內容**:
- 建立 Grafana 儀表板配置
- 設定告警規則
- 文件化監控指標

---

## Task #9: 統一快取操作

### 9.1 重構 HelloEventHandler
**目標**: 使用統一的快取服務

**修改前**:
```java
// 直接操作 CacheRepository
cacheRepos.saveOrUpdateObject(cachedKey, afterEventString);
cacheRepos.saveOrUpdateObject(taskCachedKeyForIndex, afterEventString);
```

**修改後**:
```java
// 使用抽象層
helloCacheService.updateHelloEvent(event);
```

### 9.2 快取服務整合
**目標**: Consumer 端整合 HelloCacheService

**工作內容**:
- 在 Consumer 中注入 `HelloCacheService`
- 移除所有直接的 `CacheRepository` 操作
- 確保快取操作邏輯一致

### 9.3 快取通知機制
**目標**: 實作快取更新通知

**工作內容**:
- 使用 Redis pub/sub 實作通知
- 快取更新時通知相關服務
- 實作快取一致性檢查

---

## Task #10: Consumer 業務邏輯增強

### 10.1 移除模擬邏輯
**目標**: 實作真實的業務處理

**移除**:
```java
// 移除這些模擬程式碼
int SIMULATE_DELAY = (int) (Math.random() * 10000);
Thread.sleep(SIMULATE_DELAY);
```

**實作**:
- Hello 訊息內容分析
- 訊息格式轉換和豐富化
- 業務規則驗證

### 10.2 錯誤處理增強
**目標**: 完善錯誤分類和處理

**工作內容**:
- 區分可重試錯誤 vs 永久錯誤
- 實作指數退避重試機制
- 加入錯誤告警通知

### 10.3 任務狀態管理
**目標**: 更細緻的狀態追蹤

**狀態定義**:
```java
PENDING -> PROCESSING -> COMPLETED
         -> RETRYING -> COMPLETED
         -> FAILED
```

---

## Task #11: 完善測試覆蓋

### 11.1 Consumer 單元測試
**目標**: 建立完整的測試覆蓋

**測試檔案**:
- `HelloConsumerTest.java`
- `HelloEventHandlerTest.java`
- `HelloConsumerMetricsServiceTest.java`

### 11.2 整合測試
**目標**: 端到端流程測試

**測試範圍**:
- Producer → RabbitMQ → Consumer 完整流程
- 快取一致性測試
- 錯誤處理和重試測試

### 11.3 效能測試
**目標**: 驗證系統效能

**測試項目**:
- 訊息處理吞吐量
- 併發處理能力
- 記憶體使用情況

---

## 🔧 實作順序建議

### 第一週
1. Task #7.1 - 統一配置類別
2. Task #7.2 - 動態配置支援

### 第二週
3. Task #8.1 - 建立監控服務
4. Task #9.1 - 重構快取操作

### 第三週
5. Task #10.1 - 移除模擬邏輯
6. Task #10.2 - 錯誤處理增強

### 第四週
7. Task #11.1 - 單元測試
8. Task #11.2 - 整合測試

這樣的順序確保每週都有可交付的成果，並且後續任務建立在前面任務的基礎上。