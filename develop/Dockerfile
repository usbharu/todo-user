FROM gradle:8.14.3-jdk21-jammy AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

RUN gradle dependencies --no-daemon -PuseGitVersion=false

COPY src ./src
COPY .git ./.git

RUN gradle bootJar --no-daemon -PuseGitVersion=true

FROM eclipse-temurin:21.0.8_9-jre-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar application.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]