FROM gradle:8.7.0-jdk21-alpine as builder
WORKDIR /config-service
COPY . .
RUN gradle clean bootjar --no-daemon

FROM amazoncorretto:21-alpine
RUN apk --no-cache add bash curl
EXPOSE 8888
WORKDIR /app
COPY --from=builder /config-service/build/libs/config-service-0.0.1-SNAPSHOT.jar config-service.jar
ENTRYPOINT ["java", "-jar", "config-service.jar"]