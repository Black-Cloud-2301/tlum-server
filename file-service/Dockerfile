FROM gradle:8.7.0-jdk21-alpine as builder
WORKDIR /file-service
COPY . .
RUN gradle clean bootjar --no-daemon

FROM amazoncorretto:21-alpine
RUN apk --no-cache add bash curl
EXPOSE 9001
WORKDIR /app
COPY --from=builder /file-service/build/libs/file-service-0.0.1-SNAPSHOT.jar file-service.jar
ENTRYPOINT ["java", "-jar", "file-service.jar"]