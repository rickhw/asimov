#!/bin/bash

source ./scripts/build-config.sh

## TODO: for ARM base
# mvn spring-boot:build-image \
#     -Dspring-boot.build-image.imageName=${DOCKER_IMAGE_NAME} \
#     -Dspring-boot.build-image.additional-args="--platform=linux/arm64" \
#     -DBUILD_TYPE=dev \
#     -DHASH_CODE=${HASH_CODE} \
#     -DBUILD_ID=${BUILD_ID}

# mvn spring-boot:build-image \
#     -Dspring-boot.build-image.imageName=${DOCKER_IMAGE_NAME} \
#     -DBUILD_TYPE=${BUILD_TYPE} \
#     -DHASH_CODE=${HASH_CODE} \
#     -DBUILD_ID=${BUILD_ID}


# ./login-ecr.sh

echo "Image Name: $DOCKER_IMAGE_NAME"

cd api-server
docker build . -t ${DOCKER_IMAGE_NAME}

docker push ${DOCKER_IMAGE_NAME}




# 475976321288.dkr.ecr.us-west-2.amazonaws.com/member-service:web-api_0.3.0-20230909-1105
#docker tag f57e9af54947ae6c90153fbf31c6d5a5471fcea94b4c78dc6ac76ef2b201eed0 475976321288.dkr.ecr.us-west-2.amazonaws.com/rws-dev:rws.booter_v0.2.0-dev-b20231217-0827