# HelloCacheService 整合遷移指南

## 📋 概述

將 API-Server 和 Consumer 模組中重複的 HelloCacheService 整合到 core 模組，實現統一的快取服務。

## 🎯 整合效益

### ✅ 優點
- **消除重複代碼**: 減少 ~80% 的重複實作
- **統一維護**: 單一來源的快取邏輯，降低維護成本
- **一致性保證**: 確保兩端行為完全一致
- **擴展性**: 未來其他模組可直接使用
- **測試簡化**: 只需維護一套測試

### ⚠️ 考量點
- **依賴關係**: 各模組需依賴 core 模組
- **指標整合**: 需要適配器來整合各模組的指標服務
- **配置統一**: 使用相同的配置前綴

## 🏗️ 整合架構

```
core/
├── HelloCacheConfig (統一配置)
└── HelloCacheService (統一服務)
    ├── CacheMetricsRecorder (指標介面)
    ├── CacheInvalidationListener (失效監聽器)
    └── CacheInvalidationEvent (失效事件)

api-server/
└── HelloCacheServiceAdapter (API-Server 適配器)

consumer/
└── HelloCacheServiceAdapter (Consumer 適配器)
```

## 📦 新增檔案

### Core 模組
- `core/src/main/java/com/gtcafe/asimov/system/hello/config/HelloCacheConfig.java`
- `core/src/main/java/com/gtcafe/asimov/system/hello/service/HelloCacheService.java`

### API-Server 模組
- `api-server/src/main/java/com/gtcafe/asimov/system/hello/service/HelloCacheServiceAdapter.java`

### Consumer 模組
- `consumer/src/main/java/com/gtcafe/asimov/consumer/system/hello/service/HelloCacheServiceAdapter.java`

## 🔄 遷移步驟

### 步驟 1: 更新 API-Server 模組

1. **移除舊檔案**:
   ```bash
   rm api-server/src/main/java/com/gtcafe/asimov/system/hello/config/HelloCacheConfig.java
   rm api-server/src/main/java/com/gtcafe/asimov/system/hello/service/HelloCacheService.java
   ```

2. **更新 import 語句**:
   ```java
   // 舊的
   import com.gtcafe.asimov.system.hello.config.HelloCacheConfig;
   import com.gtcafe.asimov.system.hello.service.HelloCacheService;
   
   // 新的 (core 模組)
   import com.gtcafe.asimov.system.hello.config.HelloCacheConfig;
   import com.gtcafe.asimov.system.hello.service.HelloCacheService;
   ```

3. **無需修改業務邏輯**: API 保持完全相同

### 步驟 2: 更新 Consumer 模組

1. **移除舊檔案**:
   ```bash
   rm consumer/src/main/java/com/gtcafe/asimov/consumer/system/hello/config/HelloCacheConfig.java
   rm consumer/src/main/java/com/gtcafe/asimov/consumer/system/hello/service/HelloCacheService.java
   ```

2. **更新 HelloEventHandler**:
   ```java
   // 舊的
   import com.gtcafe.asimov.consumer.system.hello.service.HelloCacheService;
   
   // 新的 (core 模組)
   import com.gtcafe.asimov.system.hello.service.HelloCacheService;
   ```

3. **更新監控配置**:
   ```java
   // 在 HelloConsumerMonitoringConfig 中
   // 舊的
   import com.gtcafe.asimov.consumer.system.hello.service.HelloCacheService;
   
   // 新的 (core 模組)
   import com.gtcafe.asimov.system.hello.service.HelloCacheService;
   ```

### 步驟 3: 更新失效處理器

```java
// 在 HelloCacheInvalidationHandler 中
// 舊的
import com.gtcafe.asimov.consumer.system.hello.service.HelloCacheService.CacheInvalidationListener;

// 新的 (core 模組)
import com.gtcafe.asimov.system.hello.service.HelloCacheService.CacheInvalidationListener;
```

### 步驟 4: 更新測試

1. **移除重複測試**:
   ```bash
   rm consumer/src/test/java/com/gtcafe/asimov/consumer/system/hello/service/HelloCacheServiceTest.java
   ```

2. **更新現有測試的 import**:
   ```java
   // 在 api-server 的測試中
   // 舊的
   import com.gtcafe.asimov.system.hello.config.HelloCacheConfig;
   import com.gtcafe.asimov.system.hello.service.HelloCacheService;
   
   // 新的 (已經是 core 模組，無需修改)
   ```

## 🔧 配置變更

### 統一配置前綴
兩個模組都使用相同的配置前綴：
```yaml
asimov:
  system:
    hello:
      cache:
        enabled: true
        primaryTtl: PT30M
        taskIndexTtl: PT24H
        lockTimeout: PT10S
        metricsEnabled: true
        failOnCacheError: false
```

## 📊 指標整合

### API-Server 端
```java
// HelloCacheServiceAdapter 會自動整合指標
// 目前記錄到 log，未來可擴展到 HelloMetricsService
```

### Consumer 端
```java
// HelloCacheServiceAdapter 會自動整合到 HelloConsumerMetricsService
metricsService.recordCacheOperation(operation, success);
```

## 🧪 測試策略

### Core 模組測試
- 在 core 模組中創建完整的 HelloCacheService 測試
- 涵蓋所有快取操作和失效通知功能

### 模組特定測試
- API-Server: 測試適配器的指標整合
- Consumer: 測試適配器的指標整合和失效處理器註冊

## 🚀 部署注意事項

1. **依賴順序**: 確保 core 模組先部署
2. **配置檢查**: 驗證兩個模組使用相同的快取配置
3. **監控驗證**: 確認指標記錄正常運作
4. **功能測試**: 驗證快取操作和失效通知功能

## 📈 效能影響

- **記憶體使用**: 減少重複類別載入
- **維護成本**: 大幅降低
- **一致性**: 提高
- **擴展性**: 增強

## 🔄 回滾計劃

如果需要回滾：
1. 恢復各模組的原始 HelloCacheService 檔案
2. 移除適配器類別
3. 恢復原始的 import 語句
4. 恢復模組特定的配置檔案

## ✅ 驗證清單

- [ ] Core 模組的 HelloCacheService 功能正常
- [ ] API-Server 模組可正常使用快取服務
- [ ] Consumer 模組可正常使用快取服務
- [ ] 快取失效通知機制運作正常
- [ ] 指標記錄功能正常
- [ ] 配置載入正確
- [ ] 測試通過
- [ ] 文檔更新完成

## 📝 後續改進

1. **指標增強**: 在 HelloMetricsService 中添加快取相關指標
2. **監控面板**: 創建統一的快取監控面板
3. **效能優化**: 根據使用情況優化快取策略
4. **文檔完善**: 補充使用範例和最佳實踐

---

**建議**: 強烈推薦進行此整合，效益明顯且風險可控。