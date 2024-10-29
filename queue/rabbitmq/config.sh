#!/bin/bash

# 設定 RabbitMQ 管理工具位置
RABBITMQ_HOST="localhost"
RABBITMQ_USER="root"
RABBITMQ_PASS="password"
RABBITMQ_PORT=15672


# 要清空的 Queue 列表
QUEUES=("platform.sayHello" "queue2" "queue3")
