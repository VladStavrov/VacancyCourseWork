FROM gradle:8.4-focal as build

WORKDIR /workspace

COPY src ./src
COPY build.gradle ./build.gradle
COPY settings.gradle ./settings.gradle

RUN gradle clean build

FROM openjdk:17-alpine

RUN adduser --system spring-boot && addgroup --system spring-boot && adduser spring-boot spring-boot
USER spring-boot

WORKDIR /app

COPY --from=build /workspace/build/libs/api-gateway-0.0.1-SNAPSHOT.jar ./api-gateway.jar

ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
