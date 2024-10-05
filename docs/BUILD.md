
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

