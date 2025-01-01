

## Service

專注處理 Biz Logic,

1. 不處理組 DTO 的任務
2. 應該沒有其他的 import, 只有 POJO
3. 不處理 request / response 等 HTTP message


## Cache

- Redis --> 準備換成 Valkey 8.0
- 都用 String 存入
- 處理 JSON / Model 用 JsonUtils 處理


## Setup development environment

- export .env for application.yaml: `export $(grep -v '^#' .env | xargs)`


## build by Gradle wrapper

```bash
## initial gradle wrapper: gen gradlew and gradlew.bat
gradle wrapper

~$ ls build/libs
api-server-0.1.0-b20240922-1341-plain.jar api-server-0.1.0-b20240922-1341.jar

## clean and build for all
./gradlew clean build


## run Springboot
./gradlew :api-server:bootRun
./gradlew :api-server:clean
./gradlew :api-server:build

```



## Run unit test

```bash
gradle :api-server:test
gradle test --tests "TenantDomainObjectTest"
gradle test --tests "HelloControllerTest"
```

