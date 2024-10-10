FROM gradle:8.7.0-jdk21-alpine as builder
WORKDIR /notificationDto
COPY . .
RUN gradle clean bootjar --no-daemon

FROM amazoncorretto:21-alpine
RUN apk --no-cache add bash curl
EXPOSE 8080
WORKDIR /app
COPY --from=builder /notificationDto/build/libs/notificationDto-0.0.1-SNAPSHOT.jar notificationDto.jar
ENTRYPOINT ["java", "-jar", "notificationDto.jar"]