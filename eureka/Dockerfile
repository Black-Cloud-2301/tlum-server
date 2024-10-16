FROM gradle:8.7.0-jdk21-alpine AS builder
WORKDIR /eureka
COPY . .
RUN --mount=type=cache,id=gradle,target=/root/.gradle \
    --mount=type=cache,id=gradle,target=/home/gradle/.gradle \
    gradle bootJar --no-daemon

FROM amazoncorretto:21-alpine
EXPOSE 8761
WORKDIR /app
COPY --from=builder /eureka/build/libs/eureka-0.0.1-SNAPSHOT.jar eureka.jar
ENTRYPOINT ["java", "-jar", "eureka.jar"]