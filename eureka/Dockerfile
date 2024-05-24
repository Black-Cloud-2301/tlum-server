FROM gradle:8.7.0-jdk21-alpine as builder
WORKDIR /eureka
COPY . .
RUN gradle clean bootjar --no-daemon

FROM amazoncorretto:21-alpine
RUN apk --no-cache add bash curl
EXPOSE 8761
WORKDIR /app
COPY --from=builder /eureka/build/libs/eureka-0.0.1-SNAPSHOT.jar eureka.jar
ENTRYPOINT ["java", "-jar", "eureka.jar"]