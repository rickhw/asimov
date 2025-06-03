#!/bin/bash

set -e

# 從 .env 讀取變數
export $(grep -v '^#' .env | xargs)

BUILD_DIR=api-server/build
JAR_FILE=$(ls $BUILD_DIR/libs/api-server-*.jar | tail -n1)
META_FILE=$BUILD_DIR/meta.json

if [[ -z "$S3_BUCKET" ]]; then
  echo "❌ 請在 .env 中定義 S3_BUCKET"
  exit 1
fi

echo "📤 Uploading $JAR_FILE and $META_FILE to s3://$S3_BUCKET/builds/"

aws s3 cp "$JAR_FILE" "s3://$S3_BUCKET/builds/"
aws s3 cp "$META_FILE" "s3://$S3_BUCKET/builds/"

echo "✅ Upload completed."
