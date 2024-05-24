FROM gradle:8.7.0-jdk21-alpine as builder
WORKDIR /auth-service
COPY . .
RUN gradle clean bootjar --no-daemon

FROM amazoncorretto:21-alpine
RUN apk --no-cache add bash curl
EXPOSE 8081
WORKDIR /app
COPY --from=builder /auth-service/build/libs/auth-service-0.0.1-SNAPSHOT.jar auth-service.jar
ENTRYPOINT ["java", "-jar", "auth-service.jar"]