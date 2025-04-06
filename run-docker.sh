#!/bin/bash

CONATAINER_NAME="asimov-api-server"

docker rm -f ${CONATAINER_NAME}

docker run -d \
    --env-file ./deployment/.env \
    --name ${CONATAINER_NAME} \
    -v ./deployment/certs:/app/resources \
    -p 8080:8080 \
    475976321288.dkr.ecr.us-west-2.amazonaws.com/asimov:api-server_v0.1.0-dev-latest

docker logs -f ${CONATAINER_NAME}