#!/bin/bash

source ../scripts/build-config.sh

docker build -t ${DOCKER_IMAGE_NAME} -t ${DOCKER_IMAGE_LATEST_NAME} .


./login-ecr.sh

docker push ${DOCKER_IMAGE_NAME}
docker push ${DOCKER_IMAGE_LATEST_NAME}


echo "Image Name: $DOCKER_IMAGE_NAME"

# 475976321288.dkr.ecr.us-west-2.amazonaws.com/member-service:web-api_0.3.0-20230909-1105
#docker tag f57e9af54947ae6c90153fbf31c6d5a5471fcea94b4c78dc6ac76ef2b201eed0 475976321288.dkr.ecr.us-west-2.amazonaws.com/rws-dev:rws.booter_v0.2.0-dev-b20231217-0827