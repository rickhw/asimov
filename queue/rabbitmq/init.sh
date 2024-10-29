#!/bin/bash

source ./config.sh

rabbitmq-plugins enable rabbitmq_management

# 創建 Exchange
rabbitmqadmin -H $RABBITMQ_HOST -P $RABBITMQ_PORT -u $RABBITMQ_USER -p $RABBITMQ_PASS declare exchange name=my_exchange type=direct durable=true

# 創建 Queue
rabbitmqadmin -H $RABBITMQ_HOST -P $RABBITMQ_PORT -u $RABBITMQ_USER -p $RABBITMQ_PASS declare queue name=my_queue durable=true

# 設置 Routing Key 並綁定 Exchange 和 Queue
rabbitmqadmin -H $RABBITMQ_HOST -P $RABBITMQ_PORT -u $RABBITMQ_USER -p $RABBITMQ_PASS declare binding source="my_exchange" destination_type="queue" destination="my_queue" routing_key="my_routing_key"
