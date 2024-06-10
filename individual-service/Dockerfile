FROM gradle:8.7.0-jdk21-alpine as builder
WORKDIR /individual-service
COPY . .
RUN gradle clean bootjar --no-daemon

FROM amazoncorretto:21-alpine
RUN apk --no-cache add bash curl
EXPOSE 9002
WORKDIR /app
COPY --from=builder /individual-service/build/libs/individual-service-0.0.1-SNAPSHOT.jar individual-service.jar
ENTRYPOINT ["java", "-jar", "individual-service.jar"]