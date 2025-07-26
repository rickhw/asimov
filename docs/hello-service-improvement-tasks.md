# Hello Service 改善任務清單

## 📊 現況分析

### API-Server vs Consumer 模組落差
- **API-Server**: 完整的驗證、監控、快取抽象層
- **Consumer**: 基本的訊息處理、硬編碼配置、缺乏監控

## 🚀 改善任務

### 🔥 高優先級 (立即處理)

#### Task #7: 統一佇列配置管理
**問題**: Producer 使用動態配置，Consumer 使用硬編碼配置
- [ ] 7.1 將 `HelloConsumerAsyncConfig` 改為使用 `HelloQueueConfig`
- [ ] 7.2 Consumer 端支援 `@ConfigurationProperties` 動態配置
- [ ] 7.3 建立配置驗證機制，確保兩端配置一致

**預估工時**: 14 小時

#### Task #8: Consumer 端監控完善
**問題**: Consumer 端缺乏業務指標監控
- [ ] 8.1 建立 `HelloConsumerMetricsService`
- [ ] 8.2 監控訊息消費速率、處理時間、DLQ 統計
- [ ] 8.3 與 Producer 端監控指標對應，實作端到端追蹤

**預估工時**: 18 小時

#### Task #9: 統一快取操作
**問題**: Consumer 直接操作 CacheRepository，Producer 使用抽象層
- [ ] 9.1 重構 `HelloEventHandler` 使用 `HelloCacheService`
- [ ] 9.2 移除直接的 `CacheRepository` 操作
- [ ] 9.3 實作快取失效通知機制

**預估工時**: 14 小時

### 🔶 中優先級 (短期內完成)

#### Task #10: Consumer 業務邏輯增強
**問題**: Consumer 只有模擬處理邏輯
- [ ] 10.1 移除模擬延遲，實作真實業務處理
- [ ] 10.2 加入詳細的錯誤分類和重試機制
- [ ] 10.3 實作任務狀態管理和進度追蹤

**預估工時**: 22 小時

#### Task #11: 完善測試覆蓋
**問題**: Consumer 端缺乏測試覆蓋
- [ ] 11.1 建立 Consumer 端單元測試
- [ ] 11.2 Producer-Consumer 整合測試
- [ ] 11.3 RabbitMQ 和快取一致性測試

**預估工時**: 22 小時

## 📁 相關檔案

### Producer 端 (api-server)
- `core/src/main/java/com/gtcafe/asimov/system/hello/config/HelloQueueConfig.java`
- `api-server/src/main/java/com/gtcafe/asimov/system/hello/service/HelloMetricsService.java`
- `api-server/src/main/java/com/gtcafe/asimov/system/hello/service/HelloCacheService.java`

### Consumer 端 (consumer)
- `consumer/src/main/java/com/gtcafe/asimov/consumer/system/hello/HelloConsumer.java`
- `consumer/src/main/java/com/gtcafe/asimov/consumer/system/hello/HelloEventHandler.java`
- `consumer/src/main/java/com/gtcafe/asimov/consumer/system/hello/config/HelloConsumerAsyncConfig.java`

## 🎯 執行順序建議

1. **Task #7** - 解決架構不一致問題
2. **Task #8** - 提升可觀測性
3. **Task #9** - 改善程式碼品質
4. **Task #10** - 完善功能實作
5. **Task #11** - 確保品質

**總預估工時**: 90 小時
**高優先級**: 46 小時 (51%)
**中優先級**: 44 小時 (49%)