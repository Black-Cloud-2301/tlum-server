FROM gradle:8.7.0-jdk21-alpine as builder
WORKDIR /file-service
COPY . .
RUN --mount=type=cache,id=gradle,target=/root/.gradle \
    --mount=type=cache,id=gradle,target=/home/gradle/.gradle \
    gradle bootJar --no-daemon

FROM amazoncorretto:21-alpine
EXPOSE 9003
WORKDIR /app
COPY --from=builder /file-service/build/libs/file-service-0.0.1-SNAPSHOT.jar file-service.jar
ENTRYPOINT ["java", "-jar", "file-service.jar"]