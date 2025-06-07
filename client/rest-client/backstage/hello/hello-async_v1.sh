#!/bin/bash

# === [設定區] ===
# 記錄 log 檔路徑（每天產生一個檔）
LOG_DIR="/tmp/log/hello-task"
mkdir -p "$LOG_DIR"
LOG_FILE="$LOG_DIR/hello-$(date '+%Y-%m-%d').log"

# 檢查參數
if [[ -z "$1" ]]; then
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] ❌ 用法錯誤：請提供 HOSTNAME" >> "$LOG_FILE"
  echo "用法: $0 <hostname>" >> "$LOG_FILE"
  exit 1
fi

HOSTNAME="$1"
CONTENT_TYPE="application/json"
REQUEST_MODE="async"

# log function with timestamp
log() {
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] $*" >> "$LOG_FILE"
}

# 隨機產生 1~30 筆請求
REQUEST_COUNT=$((RANDOM % 128 + 1))
log "🚀 準備平行發送 $REQUEST_COUNT 筆請求到 $HOSTNAME"

# === 任務 function ===
send_and_track_task() {
  local index="$1"
  local message="Hello, Asimov #$index"

  log "[#${index}] 發送請求..."

  RESPONSE=$(curl -s -X POST "$HOSTNAME/api/v1alpha/hello" \
    -H "Content-Type: $CONTENT_TYPE" \
    -H "X-Request-Mode: $REQUEST_MODE" \
    -d "{\"message\": \"$message\"}")

  TASK_ID=$(echo "$RESPONSE" | jq -r '.id')

  if [[ "$TASK_ID" == "null" || -z "$TASK_ID" ]]; then
    log "[#${index}] ❌ 無法取得 Task ID，回應如下：$RESPONSE"
    return
  fi

  log "[#${index}] ✅ 任務送出成功 Task ID = $TASK_ID"

  # 查詢狀態直到完成
  while true; do
    STATUS_RESPONSE=$(curl -s "$HOSTNAME/api/v1alpha/tasks/$TASK_ID")
    STATE=$(echo "$STATUS_RESPONSE" | jq -r '.state')

    log "[#${index}] 狀態: $STATE"

    if [[ "$STATE" == "COMPLETED" ]]; then
      log "[#${index}] ✅ 已完成"
      break
    elif [[ "$STATE" == "FAILED" ]]; then
      log "[#${index}] ❌ 任務失敗"
      break
    fi

    sleep 2
  done
}

# === 平行送出所有任務 ===
for ((i=1; i<=REQUEST_COUNT; i++)); do
  send_and_track_task "$i" &
done

wait
log "🎉 所有任務完成"
