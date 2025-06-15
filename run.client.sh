#!/bin/bash

export $(grep -v '^#' .env | xargs)

gradle :client:clean 
gradle :client:build
gradle :client:bootRun