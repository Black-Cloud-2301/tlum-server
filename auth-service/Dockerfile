FROM gradle:8.7.0-jdk21-alpine AS builder
WORKDIR /auth-service
COPY . .
RUN --mount=type=cache,id=gradle,target=/root/.gradle \
    --mount=type=cache,id=gradle,target=/home/gradle/.gradle \
    gradle bootJar --no-daemon

FROM amazoncorretto:21-alpine
EXPOSE 8081
WORKDIR /app
COPY --from=builder /auth-service/build/libs/auth-service-0.0.1-SNAPSHOT.jar auth-service.jar
ENTRYPOINT ["java", "-jar", "auth-service.jar"]