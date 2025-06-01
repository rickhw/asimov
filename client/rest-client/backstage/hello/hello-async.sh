#!/bin/bash

# 檢查參數
if [[ -z "$1" ]]; then
  echo "❌ 用法錯誤：請提供 HOSTNAME"
  echo "用法: $0 <hostname>"
  echo "範例: $0 https://rws.lab.gtcafe.com"
  exit 1
fi

# 從參數取得 HOSTNAME
HOSTNAME="$1"
CONTENT_TYPE="application/json"
MESSAGE="Hello, Asimov"
REQUEST_MODE="async"

# 發送 Hello 請求
echo "📨 Sending hello request to $HOSTNAME ..."
RESPONSE=$(curl -s -X POST "$HOSTNAME/api/v1alpha/hello" \
  -H "Content-Type: $CONTENT_TYPE" \
  -H "X-Request-Mode: $REQUEST_MODE" \
  -d "{\"message\": \"$MESSAGE\"}")

# 解析 Task ID
TASK_ID=$(echo "$RESPONSE" | jq -r '.id')

if [[ "$TASK_ID" == "null" || -z "$TASK_ID" ]]; then
  echo "❌ 無法取得 Task ID，請檢查 API 回應："
  echo "$RESPONSE"
  exit 1
fi

echo "✅ 任務已送出，Task ID = $TASK_ID"

# 查詢任務狀態直到完成
while true; do
  STATUS_RESPONSE=$(curl -s -X GET "$HOSTNAME/api/v1alpha/tasks/$TASK_ID")
  STATE=$(echo "$STATUS_RESPONSE" | jq -r '.state')

  echo "⌛ 任務狀態: $STATE"

  if [[ "$STATE" == "COMPLETED" ]]; then
    echo "✅ 任務已完成"
    echo "$STATUS_RESPONSE" | jq .
    break
  elif [[ "$STATE" == "FAILED" ]]; then
    echo "❌ 任務失敗"
    echo "$STATUS_RESPONSE" | jq .
    exit 1
  fi

  sleep 2
done
