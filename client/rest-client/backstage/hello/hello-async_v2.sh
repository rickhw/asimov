#!/bin/bash

# === [設定區] ===
LOG_DIR="/tmp/log/hello-task"
mkdir -p "$LOG_DIR"
LOG_FILE="$LOG_DIR/hello-$(date '+%Y-%m-%d').log"

# === [參數檢查與預設值] ===
if [[ -z "$1" ]]; then
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] ❌ 用法錯誤：請提供 HOSTNAME" >> "$LOG_FILE"
  echo "用法: $0 <hostname> [max_requests]" >> "$LOG_FILE"
  exit 1
fi

HOSTNAME="$1"
MAX_REQUESTS="${2:-30}"  # 預設最多送出 30 筆
CONTENT_TYPE="application/json"
REQUEST_MODE="async"

# === [統計用變數] ===
TOTAL_SENT=0
TOTAL_COMPLETED=0

# === log function ===
log() {
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] $*" >> "$LOG_FILE"
}

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
  ((TOTAL_SENT++))

  # 查詢狀態直到完成
  while true; do
    STATUS_RESPONSE=$(curl -s "$HOSTNAME/api/v1alpha/tasks/$TASK_ID")
    STATE=$(echo "$STATUS_RESPONSE" | jq -r '.state')

    log "[#${index}] 狀態: $STATE"

    if [[ "$STATE" == "COMPLETED" ]]; then
      log "[#${index}] ✅ 已完成"
      ((TOTAL_COMPLETED++))
      break
    elif [[ "$STATE" == "FAILED" ]]; then
      log "[#${index}] ❌ 任務失敗"
      break
    fi

    sleep 2
  done
}

# === 產生隨機請求數（不超過指定最大值）===
REQUEST_COUNT=$((RANDOM % MAX_REQUESTS + 1))
log "🚀 準備平行發送 $REQUEST_COUNT 筆請求到 $HOSTNAME（最大上限 $MAX_REQUESTS）"

# === 平行送出所有任務 ===
for ((i=1; i<=REQUEST_COUNT; i++)); do
  send_and_track_task "$i" &
done

wait

log "📊 統計：實際送出 $TOTAL_SENT 筆，完成 $TOTAL_COMPLETED 筆"
log "🎉 所有任務完成"
