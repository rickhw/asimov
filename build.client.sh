#!/bin/bash

set -e

# 從 .env 讀取 S3_BUCKET 等環境變數
export $(grep -v '^#' .env | xargs)

BUILD_ID=$(date +%Y%m%d-%H%M)
BUILD_TYPE=${1:-dev}

echo "🔧 Building api-server with buildType=$BUILD_TYPE and buildId=$BUILD_ID"

gradle :client:clean
gradle :client:build -PbuildType=$BUILD_TYPE -PbuildId=$BUILD_ID
