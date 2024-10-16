FROM gradle:8.7.0-jdk21-alpine AS builder
WORKDIR /gateway
COPY . .
RUN --mount=type=cache,id=gradle,target=/root/.gradle \
    --mount=type=cache,id=gradle,target=/home/gradle/.gradle \
    gradle bootJar --no-daemon

FROM amazoncorretto:21-alpine
EXPOSE 8080
WORKDIR /app
COPY --from=builder /gateway/build/libs/gateway-0.0.1-SNAPSHOT.jar gateway.jar
ENTRYPOINT ["java", "-jar", "gateway.jar"]