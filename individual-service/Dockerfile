FROM gradle:8.7.0-jdk21-alpine AS builder
WORKDIR /individual-service
COPY . .
RUN --mount=type=cache,id=gradle,target=/root/.gradle \
    --mount=type=cache,id=gradle,target=/home/gradle/.gradle \
    gradle bootJar --no-daemon

FROM amazoncorretto:21-alpine
EXPOSE 9002
WORKDIR /app
COPY --from=builder /individual-service/build/libs/individual-service-0.0.1-SNAPSHOT.jar individual-service.jar
ENTRYPOINT ["java", "-jar", "individual-service.jar"]