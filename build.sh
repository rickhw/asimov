#!/bin/bash

set -e

# 從 .env 讀取 S3_BUCKET 等環境變數
export $(grep -v '^#' .env | xargs)

BUILD_ID=$(date +%Y%m%d-%H%M)
BUILD_TYPE=${1:-dev}
MODULE=${2} # 新增的參數，用於指定模組

if [ -z "$MODULE" ]; then
  echo "Usage: $0 [build_type] <module_name>"
  echo "Example: $0 dev api-server"
  exit 1
fi

echo "🔧 Building $MODULE with buildType=$BUILD_TYPE and buildId=$BUILD_ID"

gradle :$MODULE:clean
gradle :$MODULE:build -PbuildType=$BUILD_TYPE -PbuildId=$BUILD_ID
