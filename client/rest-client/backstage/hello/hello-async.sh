#!/bin/bash

# 檢查參數
if [[ -z "$1" ]]; then
  echo "❌ 用法錯誤：請提供 HOSTNAME"
  echo "用法: $0 <hostname>"
  echo "範例: $0 https://rws.lab.gtcafe.com"
  exit 1
fi

HOSTNAME="$1"
CONTENT_TYPE="application/json"
REQUEST_MODE="async"

# 產生 1～30 的隨機數
REQUEST_COUNT=$((RANDOM % 30 + 1))

echo "🚀 準備平行發送 $REQUEST_COUNT 筆請求到 $HOSTNAME"

# 宣告任務函式
send_and_track_task() {
  local index="$1"
  local message="Hello, Asimov #$index"

  echo "[#${index}] 發送請求..."

  RESPONSE=$(curl -s -X POST "$HOSTNAME/api/v1alpha/hello" \
    -H "Content-Type: $CONTENT_TYPE" \
    -H "X-Request-Mode: $REQUEST_MODE" \
    -d "{\"message\": \"$message\"}")

  TASK_ID=$(echo "$RESPONSE" | jq -r '.id')

  if [[ "$TASK_ID" == "null" || -z "$TASK_ID" ]]; then
    echo "[#${index}] ❌ 無法取得 Task ID，回應如下：$RESPONSE"
    return
  fi

  echo "[#${index}] ✅ 任務送出成功 Task ID = $TASK_ID"

  # 查詢狀態直到完成
  while true; do
    STATUS_RESPONSE=$(curl -s "$HOSTNAME/api/v1alpha/tasks/$TASK_ID")
    STATE=$(echo "$STATUS_RESPONSE" | jq -r '.state')

    echo "[#${index}] 狀態: $STATE"

    if [[ "$STATE" == "COMPLETED" ]]; then
      echo "[#${index}] ✅ 已完成"
      break
    elif [[ "$STATE" == "FAILED" ]]; then
      echo "[#${index}] ❌ 任務失敗"
      break
    fi

    sleep 2
  done
}

# 發送所有請求（平行）
for ((i=1; i<=REQUEST_COUNT; i++)); do
  send_and_track_task "$i" &
done

# 等待所有背景任務結束
wait
echo "🎉 所有請求已完成"
