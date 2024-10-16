#FROM gradle:8.7.0-jdk21-alpine as builder
#WORKDIR /config-service
#COPY . .
#RUN gradle clean bootjar --no-daemon
#
#FROM amazoncorretto:21-alpine
#RUN apk --no-cache add bash curl
#EXPOSE 8888
#WORKDIR /app
#COPY --from=builder /config-service/build/libs/config-service-0.0.1-SNAPSHOT.jar config-service.jar
#ENTRYPOINT ["java", "-jar", "config-service.jar"]

#FROM gradle:7.6.1-jdk17-alpine as builder
#WORKDIR /app
#COPY build.gradle.kts .
#COPY gradle.properties .
#RUN echo 'rootProject.name = "revo-backend"' >> settings.gradle.kts  \
#    && echo 'include("config-server")' >> settings.gradle.kts
#COPY ./config-server ./config-server
#RUN --mount=type=cache,id=gradle,target=/root/.gradle \
#    --mount=type=cache,id=gradle,target=/home/gradle/.gradle \
#    gradle :config-server:bootJar --no-daemon
#
#FROM openjdk:17-alpine
#RUN apk --no-cache add bash curl
#EXPOSE 8888
#WORKDIR /app
#COPY --from=builder /app/config-server/build/libs/*.jar config-server.jar
#ENTRYPOINT ["java", "-jar" ,"config-server.jar"]

FROM gradle:8.7.0-jdk21-alpine AS builder
WORKDIR /config-service
COPY . .
RUN --mount=type=cache,id=gradle,target=/root/.gradle \
    --mount=type=cache,id=gradle,target=/home/gradle/.gradle \
    gradle bootJar --no-daemon

FROM amazoncorretto:21-alpine
EXPOSE 8888
WORKDIR /app
COPY --from=builder /config-service/build/libs/*.jar config-service.jar
ENTRYPOINT ["java", "-jar", "config-service.jar"]