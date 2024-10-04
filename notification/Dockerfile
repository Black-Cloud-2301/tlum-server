FROM gradle:8.7.0-jdk21-alpine as builder
WORKDIR /notification
COPY . .
RUN gradle clean bootjar --no-daemon

FROM amazoncorretto:21-alpine
RUN apk --no-cache add bash curl
EXPOSE 8080
WORKDIR /app
COPY --from=builder /notification/build/libs/notification-0.0.1-SNAPSHOT.jar notification.jar
ENTRYPOINT ["java", "-jar", "notification.jar"]