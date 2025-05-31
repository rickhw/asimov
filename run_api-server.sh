#!/bin/bash

export $(grep -v '^#' .env | xargs)

gradle :api-server:clean 
gradle :api-server:build
gradle :api-server:bootRun