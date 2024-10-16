FROM gradle:8.7.0-jdk21-alpine AS builder
WORKDIR /notification
COPY . .
RUN --mount=type=cache,id=gradle,target=/root/.gradle \
    --mount=type=cache,id=gradle,target=/home/gradle/.gradle \
    gradle bootJar --no-daemon

FROM amazoncorretto:21-alpine
EXPOSE 9004
WORKDIR /app
COPY --from=builder /notification/build/libs/notification-0.0.1-SNAPSHOT.jar notification.jar
ENTRYPOINT ["java", "-jar", "notification.jar"]