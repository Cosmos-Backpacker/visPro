# 使用华为云容器镜像服务的 OpenJDK 17 镜像作为基础镜像
FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/library/openjdk:17-jdk-slim

WORKDIR /app

# 直接从本地复制打好的 JAR 文件到容器中
COPY visPro-auth-0.0.1-SNAPSHOT.jar app.jar

# Run the web service on container startup.
CMD ["java","-jar","app.jar","--spring.profiles.active=prod"]