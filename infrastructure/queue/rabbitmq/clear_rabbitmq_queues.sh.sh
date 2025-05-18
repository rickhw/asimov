#!/bin/bash

source ./config.sh


# 迴圈遍歷所有 Queue，並清空每一個
for queue in "${QUEUES[@]}"
do
  echo "Clearing queue: $queue"
  rabbitmqadmin -H $RABBITMQ_HOST -P $RABBITMQ_PORT -u $RABBITMQ_USER -p $RABBITMQ_PASS purge queue name=$queue
  if [ $? -eq 0 ]; then
    echo "Successfully cleared $queue"
  else
    echo "Failed to clear $queue"
  fi
done
