#!/usr/bin/env python3
"""
Azure DevOps Task Creation Script
Usage: python create-azure-devops-tasks.py --org your-org --project your-project --pat your-personal-access-token
"""

import argparse
import base64
import json
import requests
import time
from typing import Dict, List, Optional

class AzureDevOpsTaskCreator:
    def __init__(self, organization: str, project: str, pat: str):
        self.organization = organization
        self.project = project
        self.base_url = f"https://dev.azure.com/{organization}/{project}/_apis/wit/workitems"
        
        # Create headers with PAT
        credentials = base64.b64encode(f":{pat}".encode()).decode()
        self.headers = {
            'Authorization': f'Basic {credentials}',
            'Content-Type': 'application/json-patch+json'
        }
    
    def create_work_item(self, title: str, description: str, priority: int, effort: int) -> Optional[Dict]:
        """Create a work item in Azure DevOps"""
        
        body = [
            {
                "op": "add",
                "path": "/fields/System.WorkItemType",
                "value": "Task"
            },
            {
                "op": "add",
                "path": "/fields/System.Title",
                "value": title
            },
            {
                "op": "add",
                "path": "/fields/System.Description",
                "value": description
            },
            {
                "op": "add",
                "path": "/fields/Microsoft.VSTS.Common.Priority",
                "value": priority
            },
            {
                "op": "add",
                "path": "/fields/Microsoft.VSTS.Scheduling.Effort",
                "value": effort
            },
            {
                "op": "add",
                "path": "/fields/System.Tags",
                "value": "hello-service;api-server;consumer;architecture"
            }
        ]
        
        try:
            response = requests.post(
                f"{self.base_url}/$Task?api-version=7.0",
                headers=self.headers,
                json=body
            )
            response.raise_for_status()
            
            result = response.json()
            print(f"✅ Created: {title} (ID: {result['id']})")
            return result
            
        except requests.exceptions.RequestException as e:
            print(f"❌ Failed to create: {title}")
            print(f"Error: {e}")
            return None
    
    def create_all_tasks(self) -> List[Dict]:
        """Create all predefined tasks"""
        
        tasks = [
            {
                "title": "Task #7.1: 建立統一的佇列配置",
                "description": """**目標**: 統一 Producer 和 Consumer 的佇列配置管理

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

**相關檔案**:
- `consumer/src/main/java/com/gtcafe/asimov/consumer/system/hello/config/HelloConsumerAsyncConfig.java`
- `core/src/main/java/com/gtcafe/asimov/system/hello/config/HelloQueueConfig.java`
- `core/src/main/java/com/gtcafe/asimov/infrastructure/queue/RabbitInitializer.java`""",
                "priority": 1,
                "effort": 8
            },
            {
                "title": "Task #7.2: 實作動態佇列配置",
                "description": """**目標**: Consumer 端支援動態佇列配置

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

**技術要求**:
- 使用 Spring Boot @ConfigurationProperties
- 實作配置變更監聽
- 加入配置驗證邏輯""",
                "priority": 1,
                "effort": 6
            },
            {
                "title": "Task #8.1: 建立 HelloConsumerMetricsService",
                "description": """**目標**: 為 Consumer 端建立完整的監控指標

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

**監控指標**:
- `hello_consumer_messages_processed_total`
- `hello_consumer_processing_duration_seconds`
- `hello_consumer_dlq_messages_total`
- `hello_consumer_task_state_transitions_total`""",
                "priority": 1,
                "effort": 10
            },
            {
                "title": "Task #8.2: 整合業務監控",
                "description": """**目標**: 整合 Producer 和 Consumer 的業務監控

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

**整合要求**:
- 與 HelloMetricsService 指標對應
- 實作分散式追蹤
- 加入 Actuator 健康檢查""",
                "priority": 1,
                "effort": 8
            },
            {
                "title": "Task #9.1: Consumer 端使用 HelloCacheService",
                "description": """**目標**: 統一快取操作，使用 HelloCacheService

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

**重構範圍**:
- `consumer/src/main/java/com/gtcafe/asimov/consumer/system/hello/HelloEventHandler.java`
- 整合 `api-server/src/main/java/com/gtcafe/asimov/system/hello/service/HelloCacheService.java`""",
                "priority": 1,
                "effort": 6
            },
            {
                "title": "Task #9.2: 快取策略優化",
                "description": """**目標**: 優化快取策略和一致性

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

**技術實作**:
- Redis pub/sub 通知機制
- 快取一致性檢查排程
- 可配置的快取策略""",
                "priority": 2,
                "effort": 8
            },
            {
                "title": "Task #10.1: 實作真實的業務處理邏輯",
                "description": """**目標**: 移除模擬邏輯，實作真實業務處理

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

**業務邏輯**:
- Hello 訊息內容分析和處理
- 訊息格式轉換和豐富化
- 業務規則引擎整合""",
                "priority": 2,
                "effort": 12
            },
            {
                "title": "Task #10.2: 增強錯誤處理",
                "description": """**目標**: 完善 Consumer 端的錯誤處理機制

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

**錯誤處理策略**:
- 可重試錯誤 vs 永久錯誤分類
- 指數退避重試機制
- 錯誤告警和通知系統""",
                "priority": 2,
                "effort": 10
            },
            {
                "title": "Task #11.1: Consumer 端單元測試",
                "description": """**目標**: 為 Consumer 端建立完整的測試覆蓋

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

**測試範圍**:
- 訊息消費邏輯測試
- 業務處理邏輯測試
- 錯誤處理和重試測試
- 配置和初始化測試""",
                "priority": 2,
                "effort": 12
            },
            {
                "title": "Task #11.2: 整合測試",
                "description": """**目標**: 建立 Producer-Consumer 整合測試

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

**測試類型**:
- 端到端流程測試
- 訊息佇列整合測試
- 快取一致性測試
- 效能和負載測試""",
                "priority": 2,
                "effort": 10
            }
        ]
        
        print("🚀 Starting Azure DevOps task creation...")
        print(f"Organization: {self.organization}")
        print(f"Project: {self.project}")
        print()
        
        created_tasks = []
        total_effort = sum(task["effort"] for task in tasks)
        
        for task in tasks:
            print(f"Creating task: {task['title']}...")
            result = self.create_work_item(
                title=task["title"],
                description=task["description"],
                priority=task["priority"],
                effort=task["effort"]
            )
            
            if result:
                created_tasks.append(result)
            
            time.sleep(1)  # Rate limiting
        
        print()
        print("📊 Summary:")
        print(f"Total tasks created: {len(created_tasks)}")
        print(f"Total estimated effort: {total_effort} hours")
        
        if created_tasks:
            print()
            print("🔗 Created task IDs:")
            for task in created_tasks:
                print(f"- {task['id']}: {task['fields']['System.Title']}")
        
        print()
        print("✨ Task creation completed!")
        
        return created_tasks

def main():
    parser = argparse.ArgumentParser(description='Create Azure DevOps tasks for Hello service improvements')
    parser.add_argument('--org', required=True, help='Azure DevOps organization name')
    parser.add_argument('--project', required=True, help='Azure DevOps project name')
    parser.add_argument('--pat', required=True, help='Personal Access Token')
    
    args = parser.parse_args()
    
    creator = AzureDevOpsTaskCreator(args.org, args.project, args.pat)
    creator.create_all_tasks()

if __name__ == "__main__":
    main()