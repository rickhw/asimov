#!/bin/bash

## docker register
REPOS_ENDPOINT="475976321288.dkr.ecr.us-west-2.amazonaws.com"


## product metadata
PRODUCT_NAME="rws"
CODE_NAME="asimov"
ROLE_NAME="api-server"
VERSION="0.1.0"
BUILD_TYPE="dev"

## -- Runtime Variables
HASH_CODE=$(git rev-parse HEAD)
BUILD_ID=$(date +%Y%m%d-%H%M)


REPOS_NAME="${CODE_NAME}"

## replace the code name with product names
if [ "${BUILD_TYPE}" == "rel" ]; then
    REPOS_NAME="${PRODUCT_NAME}"
fi

## Artifact metadata
ARTIFACT_ENDPOINT="${REPOS_ENDPOINT}/${REPOS_NAME}"
DOCKER_IMAGE_NAME="${ARTIFACT_ENDPOINT}:${ROLE_NAME}_v${VERSION}-${BUILD_TYPE}-b${BUILD_ID}"
DOCKER_IMAGE_LATEST_NAME="${ARTIFACT_ENDPOINT}:${ROLE_NAME}_v${VERSION}-${BUILD_TYPE}-latest"

