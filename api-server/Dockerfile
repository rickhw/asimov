# don't use openjdk:17- 
# FROM openjdk:17-jdk-slim

FROM eclipse-temurin:17-jdk-jammy
# FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY build/libs/*.jar app.jar

# ✅ -Xmx2g
# • 限制 JVM 最多用 2GB 記憶體，避免佔用過多系統資源。
# • 沒設定的話，JVM 可能自動吃掉整台機器的 25%～50% 記憶體。
# ✅ -Xms512m
# • 啟動時預先配置 512MB 記憶體，減少動態擴展造成的效能損耗。
# • 避免一啟動就吃太多或太少。
# ✅ -XX:+UseContainerSupport
# • 讓 JVM 正確識別自己在 container 裡，依容器記憶體上限自我調整。
# • 沒開的話，有些版本會忽略容器限制，照主機記憶體來配置。

ENTRYPOINT ["java", "-Xmx2g", "-Xms512m", "-XX:+UseContainerSupport", "-jar", "app.jar"]

# ENTRYPOINT ["java", "-jar", "app.jar"]