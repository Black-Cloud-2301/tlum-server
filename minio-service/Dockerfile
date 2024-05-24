FROM gradle:8.7.0-jdk21-alpine as builder
WORKDIR /minio-service
COPY . .
RUN gradle clean bootjar --no-daemon

FROM amazoncorretto:21-alpine
RUN apk --no-cache add bash curl
EXPOSE 9000
WORKDIR /app
COPY --from=builder /minio-service/build/libs/minio-service-0.0.1-SNAPSHOT.jar minio-service.jar
ENTRYPOINT ["java", "-jar", "minio-service.jar"]