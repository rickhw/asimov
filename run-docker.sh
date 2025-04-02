docker run -d \
    --env-file .env \
    -p 8080:8080 \
    475976321288.dkr.ecr.us-west-2.amazonaws.com/asimov:api-server_v0.1.0-dev-b20250330-1410
    