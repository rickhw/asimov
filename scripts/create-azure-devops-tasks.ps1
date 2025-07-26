# Azure DevOps Task Creation Script
# Usage: .\create-azure-devops-tasks.ps1 -Organization "your-org" -Project "your-project" -PAT "your-personal-access-token"

param(
    [Parameter(Mandatory=$true)]
    [string]$Organization,
    
    [Parameter(Mandatory=$true)]
    [string]$Project,
    
    [Parameter(Mandatory=$true)]
    [string]$PAT
)

# Base URL for Azure DevOps REST API
$baseUrl = "https://dev.azure.com/$Organization/$Project/_apis/wit/workitems"

# Create headers with PAT
$headers = @{
    'Authorization' = "Basic $([Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes(":$PAT")))"
    'Content-Type' = 'application/json-patch+json'
}

# Task definitions
$tasks = @(
    @{
        title = "Task #7.1: 建立統一的佇列配置"
        description = @"
**目標**: 統一 Producer 和 Consumer 的佇列配置管理

**工作項目**:
- 將 HelloConsumerAsyncConfig 改為使用 HelloQueueConfig
- 確保 Producer 和 Consumer 使用相同的佇列配置
- 建立配置驗證機制

**驗收標準**:
- Producer 和 Consumer 使用相同的佇列名稱、Exchange、Routing Key
- 配置變更時兩端自動同步
- 加入配置驗證，防止不匹配的配置

**優先級**: 高
**預估工時**: 8 小時
"@
        priority = 1
        effort = 8
    },
    @{
        title = "Task #7.2: 實作動態佇列配置"
        description = @"
**目標**: Consumer 端支援動態佇列配置

**工作項目**:
- Consumer 端支援 @ConfigurationProperties
- 移除硬編碼的佇列名稱和配置
- 加入配置熱更新支援

**驗收標準**:
- Consumer 可透過配置檔案動態調整佇列設定
- 支援配置熱更新，無需重啟服務
- 配置變更有適當的日誌記錄

**優先級**: 高
**預估工時**: 6 小時
"@
        priority = 1
        effort = 6
    },
    @{
        title = "Task #8.1: 建立 HelloConsumerMetricsService"
        description = @"
**目標**: 為 Consumer 端建立完整的監控指標

**工作項目**:
- 監控訊息消費速率和處理時間
- 監控 DLQ 訊息數量和類型
- 監控任務狀態轉換統計

**驗收標準**:
- 提供 Prometheus 格式的監控指標
- 包含訊息處理效能指標
- 包含錯誤和重試統計
- 與現有監控系統整合

**優先級**: 高
**預估工時**: 10 小時
"@
        priority = 1
        effort = 10
    },
    @{
        title = "Task #8.2: 整合業務監控"
        description = @"
**目標**: 整合 Producer 和 Consumer 的業務監控

**工作項目**:
- 與 Producer 端監控指標對應
- 加入端到端處理時間監控
- 實作健康檢查端點

**驗收標準**:
- Producer 和 Consumer 監控指標一致
- 可追蹤完整的訊息處理鏈路
- 提供健康檢查 API

**優先級**: 高
**預估工時**: 8 小時
"@
        priority = 1
        effort = 8
    },
    @{
        title = "Task #9.1: Consumer 端使用 HelloCacheService"
        description = @"
**目標**: 統一快取操作，使用 HelloCacheService

**工作項目**:
- 重構 HelloEventHandler 使用 HelloCacheService
- 移除直接的 CacheRepository 操作
- 確保快取操作一致性

**驗收標準**:
- Consumer 端使用統一的快取服務
- 快取操作邏輯一致
- 移除重複的快取程式碼

**優先級**: 高
**預估工時**: 6 小時
"@
        priority = 1
        effort = 6
    },
    @{
        title = "Task #9.2: 快取策略優化"
        description = @"
**目標**: 優化快取策略和一致性

**工作項目**:
- 實作快取失效通知機制
- 加入快取一致性檢查
- 優化快取更新策略

**驗收標準**:
- 快取更新有通知機制
- 定期檢查快取一致性
- 快取策略可配置

**優先級**: 中
**預估工時**: 8 小時
"@
        priority = 2
        effort = 8
    },
    @{
        title = "Task #10.1: 實作真實的業務處理邏輯"
        description = @"
**目標**: 移除模擬邏輯，實作真實業務處理

**工作項目**:
- 移除模擬延遲，加入實際的業務處理
- 實作訊息內容處理和轉換
- 加入業務規則驗證

**驗收標準**:
- 移除所有模擬和隨機延遲
- 實作有意義的業務邏輯
- 加入適當的業務驗證

**優先級**: 中
**預估工時**: 12 小時
"@
        priority = 2
        effort = 12
    },
    @{
        title = "Task #10.2: 增強錯誤處理"
        description = @"
**目標**: 完善 Consumer 端的錯誤處理機制

**工作項目**:
- 實作詳細的錯誤分類和處理
- 加入重試機制和指數退避
- 實作錯誤通知和告警

**驗收標準**:
- 錯誤有明確的分類和處理策略
- 實作智慧重試機制
- 重要錯誤有告警通知

**優先級**: 中
**預估工時**: 10 小時
"@
        priority = 2
        effort = 10
    },
    @{
        title = "Task #11.1: Consumer 端單元測試"
        description = @"
**目標**: 為 Consumer 端建立完整的測試覆蓋

**工作項目**:
- HelloConsumerTest 單元測試
- HelloEventHandlerTest 業務邏輯測試
- 配置類別測試

**驗收標準**:
- 測試覆蓋率達到 80% 以上
- 包含正常和異常情境測試
- 測試可自動化執行

**優先級**: 中
**預估工時**: 12 小時
"@
        priority = 2
        effort = 12
    },
    @{
        title = "Task #11.2: 整合測試"
        description = @"
**目標**: 建立 Producer-Consumer 整合測試

**工作項目**:
- Producer-Consumer 端到端測試
- RabbitMQ 整合測試
- 快取一致性測試

**驗收標準**:
- 端到端流程測試通過
- 訊息佇列整合測試穩定
- 快取一致性得到驗證

**優先級**: 中
**預估工時**: 10 小時
"@
        priority = 2
        effort = 10
    }
)

# Function to create work item
function Create-WorkItem {
    param(
        [string]$Title,
        [string]$Description,
        [int]$Priority,
        [int]$Effort
    )
    
    $body = @(
        @{
            op = "add"
            path = "/fields/System.WorkItemType"
            value = "Task"
        },
        @{
            op = "add"
            path = "/fields/System.Title"
            value = $Title
        },
        @{
            op = "add"
            path = "/fields/System.Description"
            value = $Description
        },
        @{
            op = "add"
            path = "/fields/Microsoft.VSTS.Common.Priority"
            value = $Priority
        },
        @{
            op = "add"
            path = "/fields/Microsoft.VSTS.Scheduling.Effort"
            value = $Effort
        },
        @{
            op = "add"
            path = "/fields/System.Tags"
            value = "hello-service;api-server;consumer;architecture"
        }
    ) | ConvertTo-Json
    
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/`$Task?api-version=7.0" -Method POST -Headers $headers -Body $body
        Write-Host "✅ Created: $Title (ID: $($response.id))" -ForegroundColor Green
        return $response
    }
    catch {
        Write-Host "❌ Failed to create: $Title" -ForegroundColor Red
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
}

# Main execution
Write-Host "🚀 Starting Azure DevOps task creation..." -ForegroundColor Cyan
Write-Host "Organization: $Organization" -ForegroundColor Yellow
Write-Host "Project: $Project" -ForegroundColor Yellow
Write-Host ""

$createdTasks = @()

foreach ($task in $tasks) {
    Write-Host "Creating task: $($task.title)..." -ForegroundColor White
    $result = Create-WorkItem -Title $task.title -Description $task.description -Priority $task.priority -Effort $task.effort
    
    if ($result) {
        $createdTasks += $result
    }
    
    Start-Sleep -Seconds 1  # Rate limiting
}

Write-Host ""
Write-Host "📊 Summary:" -ForegroundColor Cyan
Write-Host "Total tasks created: $($createdTasks.Count)" -ForegroundColor Green
Write-Host "Total estimated effort: $($tasks | Measure-Object -Property effort -Sum | Select-Object -ExpandProperty Sum) hours" -ForegroundColor Yellow

if ($createdTasks.Count -gt 0) {
    Write-Host ""
    Write-Host "🔗 Created task IDs:" -ForegroundColor Cyan
    foreach ($task in $createdTasks) {
        Write-Host "- $($task.id): $($task.fields.'System.Title')" -ForegroundColor White
    }
}

Write-Host ""
Write-Host "✨ Task creation completed!" -ForegroundColor Green