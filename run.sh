#!/bin/bash

export $(grep -v '^#' .env | xargs)

gradle clean build
gradle bootRun