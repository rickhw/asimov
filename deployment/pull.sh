#!/bin/bash

# === Configuration ===
REMOTE_META_URL="https://example.com/releases/meta.txt"
LOCAL_META_FILE="/opt/api-server/meta.txt"
DOWNLOAD_DIR="/opt/api-server"
JAR_PREFIX="api-server"
JAR_SOFTLINK="${DOWNLOAD_DIR}/api-server.jar"
SERVICE_NAME="asimov.api-server"

# === Download Remote Meta ===
TMP_REMOTE_META="/tmp/remote_meta.txt"
curl -fsSL "$REMOTE_META_URL" -o "$TMP_REMOTE_META" || {
    echo "[ERROR] 無法下載 meta file: $REMOTE_META_URL"
    exit 1
}

# === Parse build-id ===
REMOTE_BUILD_ID=$(grep '^build-id=' "$TMP_REMOTE_META" | cut -d= -f2)
if [[ ! "$REMOTE_BUILD_ID" =~ ^[0-9]{8}-[0-9]{4}$ ]]; then
    echo "[ERROR] 取得的 build-id 格式不正確: $REMOTE_BUILD_ID"
    exit 1
fi

LOCAL_BUILD_ID="00000000-0000"
if [[ -f "$LOCAL_META_FILE" ]]; then
    LOCAL_BUILD_ID=$(grep '^build-id=' "$LOCAL_META_FILE" | cut -d= -f2)
fi

# === 比對 Build ID ===
if [[ "$REMOTE_BUILD_ID" > "$LOCAL_BUILD_ID" ]]; then
    echo "[INFO] 發現新版本: $REMOTE_BUILD_ID > $LOCAL_BUILD_ID"
    
    # === 下載 Jar File 名稱 ===
    JAR_FILE_NAME="${JAR_PREFIX}-v1.0.0-dev-b${REMOTE_BUILD_ID}.jar"
    JAR_URL="https://example.com/releases/${JAR_FILE_NAME}"
    JAR_TARGET_PATH="${DOWNLOAD_DIR}/${JAR_FILE_NAME}"

    echo "[INFO] 下載新版本 JAR: $JAR_URL"
    curl -fsSL "$JAR_URL" -o "$JAR_TARGET_PATH" || {
        echo "[ERROR] 無法下載 JAR 檔案: $JAR_URL"
        exit 1
    }

    # === 更新 softlink ===
    ln -sfn "$JAR_TARGET_PATH" "$JAR_SOFTLINK"
    echo "[INFO] 已更新軟連結 -> $JAR_SOFTLINK -> $JAR_FILE_NAME"

    # === 儲存新的 meta file ===
    mv "$TMP_REMOTE_META" "$LOCAL_META_FILE"

    # === 重啟服務 ===
    echo "[INFO] 重新啟動服務: $SERVICE_NAME"
    sudo systemctl restart "$SERVICE_NAME" || {
        echo "[ERROR] 無法重啟服務 $SERVICE_NAME"
        exit 1
    }

    echo "[INFO] 更新完成"
else
    echo "[INFO] 無需更新，目前版本已是最新 ($LOCAL_BUILD_ID)"
fi
