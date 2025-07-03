#!/bin/bash

export $(grep -v '^#' .env | xargs)

MODULE=${1} # 新增的參數，用於指定模組

if [ -z "$MODULE" ]; then
  echo "Usage: $0 <module_name>"
  echo "Example: $0 api-server"
  exit 1
fi

echo "🚀 Running $MODULE..."

gradle :$MODULE:clean
gradle :$MODULE:build
gradle :$MODULE:bootRun
