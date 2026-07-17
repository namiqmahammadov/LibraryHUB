# ---------- Build Stage ----------
FROM gradle:9.5.0-jdk21 AS builder

WORKDIR /app

COPY . .

RUN gradle clean bootJar --no-daemon

# ---------- Run Stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]