#!/bin/bash

aws ecr get-login-password --region us-west-2 | docker login --username AWS --password-stdin 475976321288.dkr.ecr.us-west-2.amazonaws.com

